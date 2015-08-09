package singularity.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import singularity.domain.Notification;
import singularity.domain.Party;
import singularity.domain.User;
import singularity.dto.out.SessionUser;
import singularity.enums.NotificationStatus;
import singularity.exception.FailedAddingGroupMemberException;
import singularity.exception.FailedDeleteGroupException;
import singularity.exception.FailedUpdatePartyException;
import singularity.exception.GroupMemberException;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.repository.PartyRepository;
import singularity.repository.UserRepository;
import singularity.utility.RandomFactory;

@Service
@Transactional
public class PartyService {
	@Resource
	private PartyRepository partyRepository;
	@Resource
	private UserRepository userRepository;

	public List<Party> findAllByUserId(String userId) {
		return partyRepository.findAllByUsers(userRepository.findOne(userId));
	}

	public SessionUser findAdminUser(String partyId) {
		Party party = partyRepository.findOne(partyId);
		return new SessionUser(party.getAdminUser());
	}

	public Party create(String partyName, String adminUserId, String status) {
		User adminUser = userRepository.findOne(adminUserId);
		List<User> users = new ArrayList<User>();
		users.add(adminUser);
		return partyRepository.save(new Party(createPartyId(), partyName, users, adminUser, status));
	}

	private String createPartyId() {
		String partyId = RandomFactory.getRandomId(5);
		Party party = partyRepository.findOne(partyId);
		if (null != party) {
			return createPartyId();
		}
		return partyId;
	}

	public void delete(String partyId, String userId) throws FailedDeleteGroupException {
		User user = userRepository.findOne(userId);
		if (!partyRepository.exists(partyId)) {
			throw new FailedDeleteGroupException("그룹이 존재하지 않습니다.");
		}
		Party party = partyRepository.findOne(partyId);
		if (!party.isAdmin(user)) {
			throw new FailedDeleteGroupException("그룹장만 그룹을 삭제할 수 있습니다.");
		}
		partyRepository.delete(partyId);
	}

	public void inviteMember(String sessionUserId, String userId, String partyId)
			throws UnpermittedAccessGroupException, FailedAddingGroupMemberException {
		Party party = partyRepository.findOne(partyId);
		User user = userRepository.findOne(userId);
		User sessionUser = userRepository.findOne(sessionUserId);
		if (!party.hasUser(sessionUser)) {
			throw new UnpermittedAccessGroupException("회원을 초대할 권한이 없습니다!");
		}
		if (null == user) {
			throw new FailedAddingGroupMemberException("사용자를 찾을 수 없습니다!");
		}
		if (party.hasUser(user)) {
			throw new FailedAddingGroupMemberException("이미 가입되어 있습니다!");
		}
		user.getNotification().add(new Notification(user, party, NotificationStatus.INVITE));
	}

	public void joinMember(String sessionUserId, String partyId) {
		Party party = partyRepository.findOne(partyId);
		if ("F" == party.getStatus()) {
			throw new UnpermittedAccessGroupException();
		}
		// TODO if (알람을 확인하여 이미 가입 요청을 했을 경우에 대한 처리) {
		// 알람은 어드민 유저가 가지고 있다.
		// throw new FailedAddingGroupMemberException("가입 승인 대기중 입니다!");
		// }
		
		// TODO 가입 요청에 해당하는 유저를 추가한다.
	}

	public Party addMember(String userId, String partyId) {
		User user = userRepository.findOne(userId);
		Party party = partyRepository.findOne(partyId);
		party.addUser(user);
		return party;
	}

	public void leaveParty(String userId, String partyId) {
		Party party = partyRepository.findOne(partyId);
		User user = userRepository.findOne(userId);
		if (!party.hasUser(user)) {
			throw new GroupMemberException("그룹멤버가 아닙니다.");
		}
		if (userId.equals(party.getAdminUser().getId())) {
			throw new GroupMemberException("그룹장은 탈퇴가 불가능합니다.");
		}
		party.deleteUser(user);
	}

	public void deleteMember(String sessionUserId, String userId, String partyId) throws GroupMemberException {
		Party party = partyRepository.findOne(partyId);
		String adminUserId = party.getAdminUser().getId();
		if (!adminUserId.equals(sessionUserId)) {
			throw new GroupMemberException("그룹장만이 추방이 가능합니다.");
		}
		if (userId.equals(adminUserId)) {
			throw new GroupMemberException("그룹장은 탈퇴가 불가능합니다.");
		}
		party.deleteUser(userRepository.findOne(userId));
	}

	public List<User> readMembers(String partyId) {
		return partyRepository.findOne(partyId).getUsers();
	}

	public Party findOne(String partyId) {
		return partyRepository.findOne(partyId);
	}

	public void update(String sessionUserId, Party party, String rootPath, MultipartFile partyImage) {
		Party dbParty = this.findOne(party.getPartyId());
		if (!sessionUserId.equals(dbParty.getAdminUser().getId())) {
			throw new FailedUpdatePartyException("그룹장만이 그룹설정이 가능합니다.");
		}
		if (userRepository.findOne(party.getAdminUser().getId()) == null) {
			throw new FailedUpdatePartyException("존재하지 않는 사용자입니다.");
		}
		if (!dbParty.hasUser(userRepository.findOne(sessionUserId))) {
			throw new FailedUpdatePartyException("그룹멤버가 아닙니다.");
		}
		boolean isDefaultImage = "background-default.png".equals(party.getPartyImage());
		boolean isChangedImage = party.getPartyId().equals(party.getPartyImage());
		if (!isDefaultImage && !isChangedImage && !partyImage.isEmpty()) {
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
