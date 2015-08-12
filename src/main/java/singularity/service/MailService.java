package singularity.service;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import singularity.domain.Confirm;
import singularity.exception.FailedSendingEmailException;
import singularity.repository.ConfirmRepository;
import singularity.utility.RandomFactory;

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
			String htmlMsg = "<h3>Welcome to the Singularity</h3>"
					+ "<a href='http://localhost:8080/user/confirm/" + confirm.getSigningKey() + "' style='font-size: 15px;"
					+ "color: white; text-decoration:none'>"
					+ "<div style='padding: 10px; border: 0px; width: 150px;"
					+ "margin: 15px 5px; background-color: red; "
					+ "text-align:center'>find Singularity for Me</div></a>"
					+ "<p>Copyright &copy; by hyvä. All rights reserved.</p>";
			
			messageHelper.setTo(confirm.getUser().getId());
			messageHelper.setFrom("hakimaru@naver.com","singularityfor.me");
			messageHelper.setSubject("환영합니다. " + confirm.getUser().getName() + "님! Singularity 가입 인증 메일입니다.");
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
			messageHelper.setFrom("hakimaru@naver.com","Singularityfor.me");
			messageHelper.setSubject("Singularity의 임시 비밀번호를 보내드립니다.");
			messageHelper.setText(
					"임시 비밀번호는 " + tempPassword + " 입니다."
					+"<a href='http://localhost:8080/' style='font-size: 15px;"
		    		+ "color: white; text-decoration:none'>"
		    		+ "<div style='padding: 10px; border: 0px; width: 120px;"
		    		+ "margin: 15px 5px; background-color: red; "
		    		+ "text-align:center'>Singularity 가기</div></a>"
		    		+ "<p>Copyright &copy; by hyvä. All rights reserved.</p>"
		    		, true);
			javaMailSender.send(message);
		} catch (MessagingException | NullPointerException | MailAuthenticationException | UnsupportedEncodingException e) {
			throw new FailedSendingEmailException(e.getClass().getSimpleName());
		}
	}
}
