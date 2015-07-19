package stag.service;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import stag.domain.Confirm;
import stag.exception.FailedSendingEmailException;
import stag.repository.ConfirmRepository;
import stag.utility.RandomFactory;

@Service
public class MailService {
	@Resource
	private JavaMailSender javaMailSender;
	@Resource
	private ConfirmRepository confirmRepository;
	
	
	public Confirm create(Confirm confirm) {
		confirm.setSigningKey(this.createSigningKey());
		return confirmRepository.save(confirm);
	}
	
	public Confirm findOne(long confirmId) {
		return confirmRepository.findOne(confirmId);
	}
	
	public Confirm findOneBySigningKey(String signingKey) {
		return confirmRepository.findOneBySigningKey(signingKey);
	}
	
	public String createSigningKey() {
		String signingKey = RandomFactory.getRandomId(10);
		if(null != this.findOneBySigningKey(signingKey)) {
			return createSigningKey();
		}
		return signingKey;
	}

	public void sendMailforSignUp(Confirm confirm) throws FailedSendingEmailException {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "utf-8");
			String htmlMsg = "<h3>페이퍼민트에 가입해주셔서 감사합니다.</h3>"
					+ "<a href='http://localhost:8080/user/confirm/" + confirm.getSigningKey() + "' style='font-size: 15px;"
					+ "color: white; text-decoration:none'>"
					+ "<div style='padding: 10px; border: 0px; width: 150px;"
					+ "margin: 15px 5px; background-color: #74afad; "
					+ "text-align:center'>페이퍼민트 시작하기</div></a>"
					+ "<p>Copyright &copy; by link413. All rights reserved.</p>";
			
			messageHelper.setTo(confirm.getUser().getId());
			messageHelper.setFrom("hakimaru@naver.com","페이퍼민트");
			messageHelper.setSubject("환영합니다. " + confirm.getUser().getName() + "님! 페이퍼민트 가입 인증 메일입니다.");
			messageHelper.setText(htmlMsg, true);
			javaMailSender.send(message);
		} catch (MessagingException | NullPointerException | MailAuthenticationException | UnsupportedEncodingException e) {
			throw new FailedSendingEmailException(e.getClass().getSimpleName());
		}
	}
	
	public void sendMailforInitPassword(String tempPassword, String userId) throws FailedSendingEmailException {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setTo(userId);
			messageHelper.setFrom("hakimaru@naver.com","페이퍼민트");
			messageHelper.setSubject("페이퍼민트 임시 비밀번호를 보내드립니다.");
			messageHelper.setText(
					"임시 비밀번호는 " + tempPassword + " 입니다."
					+"<a href='http://localhost:8080/' style='font-size: 15px;"
		    		+ "color: white; text-decoration:none'>"
		    		+ "<div style='padding: 10px; border: 0px; width: 120px;"
		    		+ "margin: 15px 5px; background-color: #74afad; "
		    		+ "text-align:center'>페이퍼민트로 가기</div></a>"
		    		+ "<p>Copyright &copy; by link413. All rights reserved.</p>"
		    		, true);
			javaMailSender.send(message);
		} catch (MessagingException | NullPointerException | MailAuthenticationException | UnsupportedEncodingException e) {
			throw new FailedSendingEmailException(e.getClass().getSimpleName());
		}
	}
}
