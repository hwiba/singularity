package stag.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import stag.domain.Crowd;
import stag.domain.User;
import stag.exception.FailedAddingGroupMemberException;
import stag.exception.FailedDeleteGroupException;
import stag.exception.FailedUpdateGroupException;
import stag.exception.GroupMemberException;
import stag.repository.GroupRepository;
import stag.repository.UserRepository;
import stag.utility.RandomFactory;

@Service
@Transactional
public class GroupService {
	private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

	@Resource
	private GroupRepository groupRepository;
	@Resource
	private UserRepository userRepository;
	//@Resource
	//private AlarmRepository alarmpRepository;

	public List<Crowd> readGroups(String userId) {
		return groupRepository.findAllByUsers(userRepository.findOne(userId));
	}

	public Crowd create(String groupName, String adminUserId, String status) {
		User adminUser = userRepository.findOne(adminUserId);
		List<User> users = new ArrayList<User>();
		users.add(adminUser);
		return groupRepository.save(new Crowd(createGroupId(), groupName, users, adminUser, status));
	}

	private String createGroupId() {
		String groupId = RandomFactory.getRandomId(5);
		Crowd group = groupRepository.findOne(groupId);
		if (null != group) {
			return createGroupId();
		}
		return groupId;
	}

	public void delete(String groupId, String userId) {
		logger.debug("groupId: {}", groupId);
		Crowd group = groupRepository.findOne(groupId);
		if (group == null) {
			throw new FailedDeleteGroupException("그룹이 존재하지 않습니다.");
		}
		if (!group.checkCaptain(userId)) {
			throw new FailedDeleteGroupException("그룹장만 그룹을 삭제할 수 있습니다.");
		}
		groupRepository.delete(groupId);
		//TODO alarmDao.deleteGroupByGroupId(groupId);
	}

	public void inviteGroupMember(String sessionUserId, String userId, String groupId) {
		// TODO sessionUserId가 groupId에 가입이 되어있지 않을경우 
		//throw new UnpermittedAccessGroupException();
		if (null == userRepository.findOne(userId))
			throw new FailedAddingGroupMemberException("사용자를 찾을 수 없습니다!");
		if (null != groupRepository.findOneByGroupIdAndUsers(userRepository.findOne(userId), groupId))
			throw new FailedAddingGroupMemberException("이미 가입되어 있습니다!");
//		TODO if (alarmDao.checkGroupAlarms(userId, groupId))
//			throw new FailedAddingGroupMemberException("가입 요청 대기중 입니다!");
		//TODO Alarm alarm = new Alarm(createAlarmId(), "I", (new User(sessionUserId)).createSessionUser(), new User(userId), new Group(groupId));
		//TODO alarmDao.createGroupInvitation(alarm);
	}

	public void joinGroupMember(String sessionUserId, String groupId) {
		// TODO groupId가 공개 그룹이 아닐경우
		//throw new UnpermittedAccessGroupException();
		
//		TODO if (alarmDao.checkJoinedGroupAlarms(sessionUserId, groupId))
//			throw new FailedAddingGroupMemberException("가입 승인 대기중 입니다!");
		Crowd group = groupRepository.findOne(groupId);
		User adminUser = group.getAdminUser();
		//Alarm alarm = new Alarm(createAlarmId(), "J", (new User(sessionUserId)).createSessionUser(), adminUser, group);
		//alarmDao.createGroupInvitation(alarm);
	}
	
	public Crowd addGroupMember(String userId, String groupId) {
		User user = userRepository.findOne(userId);
		Crowd group = groupRepository.findOne(groupId);
		List<User> users = group.getUsers();
		users.add(user);
		group.setUsers(users);
		return groupRepository.save(group);
	}

	public void leaveGroup(String userId, String groupId) {
		Crowd group = groupRepository.findOne(groupId);
		User user = userRepository.findOne(userId);
		if (!checkGroupMember(group, user)) {
			throw new GroupMemberException("그룹멤버가 아닙니다.");
		}
		
		if (userId.equals(group.getAdminUser().getId())) {
			throw new GroupMemberException("그룹장은 탈퇴가 불가능합니다.");
		}
		this.deleteGroupUser(user, group);
	}
	
	public void deleteGroupUser(User user, Crowd group) {
		List<User> users = group.getUsers();
		users.remove(user);
		group.setUsers(users);
		groupRepository.save(group);
	}
	
	public boolean checkGroupMember(Crowd group, User user) {
		List<User> users = group.getUsers();
		for (User userX : users) {
			if (userX.equals(user)) {
				return true;
			}
		}
		return false;
	}

	public void deleteGroupMember(String sessionUserId, String userId, String groupId) {
		Crowd group = groupRepository.findOne(groupId);
		String adminUserId = group.getAdminUser().getId();
		if (!adminUserId.equals(sessionUserId)) {
			throw new GroupMemberException("그룹장만이 추방이 가능합니다.");
		}
		if (userId.equals(adminUserId)) {
			throw new GroupMemberException("그룹장은 탈퇴가 불가능합니다.");
		}
		this.deleteGroupUser(userRepository.findOne(userId), group);
	}
	
	public List<User> groupMembers(String groupId) {
		return groupRepository.findOne(groupId).getUsers();
	}

	public Crowd readGroup(String groupId) {
		return groupRepository.findOne(groupId);
	}

//	private String createAlarmId() {
//		String alarmId = RandomFactory.getRandomId(10);
//		if (alarmDao.isExistAlarmId(alarmId)) {
//			return createAlarmId();
//		}
//		return alarmId;
//	}

	public void update(String sessionUserId, Crowd group, String rootPath, MultipartFile groupImage) {
		Crowd dbGroup = this.readGroup(group.getGroupId());
		if (!sessionUserId.equals(dbGroup.getAdminUser().getId())) {
			throw new FailedUpdateGroupException("그룹장만이 그룹설정이 가능합니다.");
		}
		if (userRepository.findOne(group.getAdminUser().getId()) == null) {
			throw new FailedUpdateGroupException("존재하지 않는 사용자입니다.");
		}
		if (!this.checkGroupMember(dbGroup, userRepository.findOne(sessionUserId))) {
			throw new FailedUpdateGroupException("그룹멤버가 아닙니다.");
		}

		boolean isDefaultImage = "background-default.png".equals(group.getGroupImage());
		boolean isChangedImage = group.getGroupId().equals(group.getGroupImage());
		
		if(!isDefaultImage && !isChangedImage && !groupImage.isEmpty()) {
			try {
				String fileName = group.getGroupId();
				groupImage.transferTo(new File(rootPath + "img/group/" + fileName));
				group.setGroupImage(fileName);
			} catch (IOException e) {
				throw new FailedUpdateGroupException("잘못된 형식입니다.");
			}
		}
		groupRepository.save(group);
	}
}
