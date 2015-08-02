package singularity.service;

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

import singularity.domain.Party;
import singularity.domain.User;
import singularity.dto.out.SessionUser;
import singularity.exception.FailedAddingGroupMemberException;
import singularity.exception.FailedDeleteGroupException;
import singularity.exception.FailedUpdatePartyException;
import singularity.exception.GroupMemberException;
import singularity.repository.PartyRepository;
import singularity.repository.UserRepository;
import singularity.utility.RandomFactory;

@Service
@Transactional
public class PartyService {
	private static final Logger logger = LoggerFactory.getLogger(PartyService.class);

	@Resource
	private PartyRepository partyRepository;
	@Resource
	private UserRepository userRepository;
	//@Resource
	//private AlarmRepository alarmpRepository;

	public List<Party> readGroups(String userId) {
		return partyRepository.findAllByUsers(userRepository.findOne(userId));
	}
	
	public SessionUser readCaptainUser(String groupId) {
		Party group = partyRepository.findOne(groupId);
		return new SessionUser(group.getAdminUser());
	}

	public Party create(String groupName, String adminUserId, String status) {
		User adminUser = userRepository.findOne(adminUserId);
		List<User> users = new ArrayList<User>();
		users.add(adminUser);
		return partyRepository.save(new Party(createGroupId(), groupName, users, adminUser, status));
	}

	private String createGroupId() {
		String groupId = RandomFactory.getRandomId(5);
		Party group = partyRepository.findOne(groupId);
		if (null != group) {
			return createGroupId();
		}
		return groupId;
	}

	public void delete(String partyId, String userId) {
		if (!partyRepository.exists(partyId)) {
			throw new FailedDeleteGroupException("그룹이 존재하지 않습니다.");
		}
		Party party = partyRepository.findOne(partyId);
		if (!party.checkCaptain(userId)) {
			throw new FailedDeleteGroupException("그룹장만 그룹을 삭제할 수 있습니다.");
		}
		partyRepository.delete(partyId);
		//TODO alarmDao.deleteGroupByGroupId(groupId);
	}

	public void inviteGroupMember(String sessionUserId, String userId, String groupId) {
		// TODO sessionUserId가 groupId에 가입이 되어있지 않을경우 
		//throw new UnpermittedAccessGroupException();
		if (null == userRepository.findOne(userId))
			throw new FailedAddingGroupMemberException("사용자를 찾을 수 없습니다!");
		if (null != partyRepository.findOneByGroupIdAndUsers(userRepository.findOne(userId), groupId))
			throw new FailedAddingGroupMemberException("이미 가입되어 있습니다!");
//		TODO if (alarmDao.checkGroupAlarms(userId, groupId))
//			throw new FailedAddingGroupMemberException("가입 요청 대기중 입니다!");
		//TODO Alarm alarm = new Alarm(createAlarmId(), "I", (new User(sessionUserId)).createSessionUser(), new User(userId), new Group(groupId));
		//TODO alarmDao.createGroupInvitation(alarm);
	}

	public void joinGroupMember(String sessionUserId, String partyId) {
		// TODO partyId가 공개 그룹이 아닐경우
		//throw new UnpermittedAccessGroupException();
		
//		TODO if (alarmDao.checkJoinedGroupAlarms(sessionUserId, groupId))
//			throw new FailedAddingGroupMemberException("가입 승인 대기중 입니다!");
		Party party = partyRepository.findOne(partyId);
		//User adminUser = party.getAdminUser();
		//Alarm alarm = new Alarm(createAlarmId(), "J", (new User(sessionUserId)).createSessionUser(), adminUser, group);
		//alarmDao.createGroupInvitation(alarm);
	}
	
	public Party addMember(String userId, String partyId) {
		User user = userRepository.findOne(userId);
		Party party = partyRepository.findOne(partyId);
		List<User> users = party.getUsers();
		users.add(user);
		party.setUsers(users);
		return partyRepository.save(party);
	}

	public boolean checkMember(Party group, User user) {
		logger.warn("groupdd = {}", group);
		List<User> users = group.getUsers();
		for (User userX : users) {
			if (userX.equals(user)) {
				return true;
			}
		}
		return false;
	}
	
	public void leaveParty(String userId, String groupId) {
		Party group = partyRepository.findOne(groupId);
		User user = userRepository.findOne(userId);
		if (!checkMember(group, user)) {
			throw new GroupMemberException("그룹멤버가 아닙니다.");
		}
		
		if (userId.equals(group.getAdminUser().getId())) {
			throw new GroupMemberException("그룹장은 탈퇴가 불가능합니다.");
		}
		this.deleteUser(user, group);
	}

	public void deleteMember(String sessionUserId, String userId, String groupId) {
		Party group = partyRepository.findOne(groupId);
		String adminUserId = group.getAdminUser().getId();
		if (!adminUserId.equals(sessionUserId)) {
			throw new GroupMemberException("그룹장만이 추방이 가능합니다.");
		}
		if (userId.equals(adminUserId)) {
			throw new GroupMemberException("그룹장은 탈퇴가 불가능합니다.");
		}
		this.deleteUser(userRepository.findOne(userId), group);
	}
	
	private void deleteUser(User user, Party group) {
		List<User> users = group.getUsers();
		users.remove(user);
		group.setUsers(users);
		partyRepository.save(group);
	}
	
	public List<User> readMembers(String groupId) {
		return partyRepository.findOne(groupId).getUsers();
	}

	public Party findOne(String groupId) {
		return partyRepository.findOne(groupId);
	}

//	private String createAlarmId() {
//		String alarmId = RandomFactory.getRandomId(10);
//		if (alarmDao.isExistAlarmId(alarmId)) {
//			return createAlarmId();
//		}
//		return alarmId;
//	}

	public void update(String sessionUserId, Party party, String rootPath, MultipartFile partyImage) {
		Party dbGroup = this.findOne(party.getPartyId());
		if (!sessionUserId.equals(dbGroup.getAdminUser().getId())) {
			throw new FailedUpdatePartyException("그룹장만이 그룹설정이 가능합니다.");
		}
		if (userRepository.findOne(party.getAdminUser().getId()) == null) {
			throw new FailedUpdatePartyException("존재하지 않는 사용자입니다.");
		}
		if (!this.checkMember(dbGroup, userRepository.findOne(sessionUserId))) {
			throw new FailedUpdatePartyException("그룹멤버가 아닙니다.");
		}

		boolean isDefaultImage = "background-default.png".equals(party.getPartyImage());
		boolean isChangedImage = party.getPartyId().equals(party.getPartyImage());
		
		if(!isDefaultImage && !isChangedImage && !partyImage.isEmpty()) {
			try {
				String fileName = party.getPartyId();
				partyImage.transferTo(new File(rootPath + "img/group/" + fileName));
				party.setPartyImage(fileName);
			} catch (IOException e) {
				throw new FailedUpdatePartyException("잘못된 형식입니다.");
			}
		}
		partyRepository.save(party);
	}
}
