package singularity.party.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import singularity.exception.FailedAddingGroupMemberException;
import singularity.exception.FailedUpdatePartyException;
import singularity.exception.PartyLeaveFailedException;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.notification.domain.Notification;
import singularity.notification.repository.NotificationRepository;
import singularity.party.domain.Party;
import singularity.party.repository.PartyRepository;
import singularity.user.domain.User;
import singularity.user.repository.UserRepository;

@Service
@Transactional
public class PartyService {
	@Resource
	private PartyRepository partyRepository;
	@Resource
	private UserRepository userRepository;
	@Resource
	private NotificationRepository notificationRepository;

	public List<Party> findAllByUserId(Long userId) {
		return partyRepository.findAllByMembers(userRepository.findOne(userId));
	}

	public User findAdmin(Long partyId) {
		return partyRepository.findOne(partyId).getAdmin();
	}

	public Party create(String partyName, Long adminUserId, Party.Openness openness) {
		User admin = userRepository.findOne(adminUserId);
		return partyRepository.save(new Party(new Date(), partyName, null, admin, openness));
	}

	public void delete(Long partyId, Long userId) throws IllegalArgumentException {
		User user = userRepository.findOne(userId);
		if (!partyRepository.exists(partyId)) {
			throw new IllegalArgumentException("그룹이 존재하지 않습니다.");
		}
		Party party = partyRepository.findOne(partyId);
		if (!party.isAdmin(user)) {
			throw new IllegalArgumentException("그룹장만 그룹을 삭제할 수 있습니다.");
		}
		party.close();
	}

	public void inviteMember(Long sessionUserId, Long userId, Long partyId)
			throws UnpermittedAccessGroupException, FailedAddingGroupMemberException {
		Party party = partyRepository.findOne(partyId);
		User user = userRepository.findOne(userId);
		User sessionUser = userRepository.findOne(sessionUserId);
		if (!party.hasMember(sessionUser)) {
			throw new UnpermittedAccessGroupException("회원을 초대할 권한이 없습니다!");
		}
		if (null == user) {
			throw new FailedAddingGroupMemberException("사용자를 찾을 수 없습니다!");
		}
		if (party.hasMember(user)) {
			throw new FailedAddingGroupMemberException("이미 가입되어 있습니다!");
		}
		notificationRepository
				.save(new Notification(new Date(), sessionUser, user, party, Notification.Pattern.INVITE));
	}

	// TODO 메서드가 비대함. 간략화 할 것.
	public void joinMember(Long sessionUserId, Long partyId) {
		Party party = partyRepository.findOne(partyId);
		User writer = userRepository.findOne(sessionUserId);
		if (party.isClose()) {
			throw new UnpermittedAccessGroupException();
		}
		if (party.hasMember(writer)) {
			throw new FailedAddingGroupMemberException("이미 가입한 유저입니다!");
		}
		if (null != notificationRepository.findOneByPartyAndWriterAndReader(party, writer, party.getAdmin()).stream()
				.filter(n -> n.isRequest()).findFirst()) {
			throw new FailedAddingGroupMemberException("가입 승인 대기중 입니다!");
		}
		notificationRepository.save(new Notification(new Date(), writer, party.getAdmin(), party, Notification.Pattern.REQUEST));
	}

	public void addMember(Long userId, Long partyId) {
		User user = userRepository.findOne(userId);
		Party party = partyRepository.findOne(partyId);
		party.addMember(user);
	}

	public void leaveParty(Long userId, Long partyId) {
		Party party = partyRepository.findOne(partyId);
		User user = userRepository.findOne(userId);
		if (!party.hasMember(user)) {
			throw new PartyLeaveFailedException("그룹멤버가 아닙니다.");
		}
		if (userId.equals(party.getAdmin().getId())) {
			throw new PartyLeaveFailedException("그룹장은 탈퇴가 불가능합니다.");
		}
		party.deleteMember(user);
	}

	public void deleteMember(Long sessionUserId, Long userId, Long partyId) throws PartyLeaveFailedException {
		Party party = partyRepository.findOne(partyId);
		Long adminUserId = party.getAdmin().getId();
		if (!adminUserId.equals(sessionUserId)) {
			throw new PartyLeaveFailedException("그룹장만이 추방이 가능합니다.");
		}
		if (userId.equals(adminUserId)) {
			throw new PartyLeaveFailedException("그룹장은 탈퇴가 불가능합니다.");
		}
		party.deleteMember(userRepository.findOne(userId));
	}

	public List<User> readMembers(Long partyId) {
		return partyRepository.findOne(partyId).getMembers();
	}

	public Party findOne(Long partyId) {
		return partyRepository.findOne(partyId);
	}

	// TODO 메서드가 비대하므로 분리할 것.
	public void update(Long sessionUserId, Party party, String rootPath, MultipartFile partyImage) {
		Party dbParty = this.findOne(party.getId());
		User user = userRepository.findOne(sessionUserId);

		if (!dbParty.isAdmin(user)) {
			throw new FailedUpdatePartyException("그룹장만이 그룹설정이 가능합니다.");
		}
		if (userRepository.findOne(party.getAdmin().getId()) == null) {
			throw new FailedUpdatePartyException("존재하지 않는 사용자입니다.");
		}
		if (!dbParty.hasMember(userRepository.findOne(sessionUserId))) {
			throw new FailedUpdatePartyException("그룹멤버가 아닙니다.");
		}
		boolean isDefaultImage = "background-default.png".equals(party.getBackgroundImage());
		boolean isChangedImage = party.getId().equals(party.getBackgroundImage());
		if (!isDefaultImage && !isChangedImage && !partyImage.isEmpty()) {
			try {
				String fileName = party.getBackgroundImage();
				partyImage.transferTo(new File(rootPath + "img/group/" + fileName));
				party.setBackgroundImage(fileName);
			} catch (IOException e) {
				throw new FailedUpdatePartyException("잘못된 형식입니다.");
			}
		}
	}
}
