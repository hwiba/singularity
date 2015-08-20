package singularity.user.service;

import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import singularity.common.utility.RandomFactory;
import singularity.exception.FailedSendingEmailException;
import singularity.user.domain.Confirm;
import singularity.user.domain.User;
import singularity.user.repository.ConfirmRepository;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class ConfirmService {
	@Resource
	private JavaMailSender javaMailSender;
	@Resource
	private ConfirmRepository confirmRepository;


    private String createIdentificationKey() {
        String identificationKey = RandomFactory.getRandomId(10);
        if(null != this.findOneByIdentificationKey(identificationKey)) {
            return createIdentificationKey();
        }
        return identificationKey;
    }

	public Confirm create(User user) {
        Confirm confirm = new Confirm(new Date(), user, this.createIdentificationKey());
		return confirmRepository.save(confirm);
	}
	
	public Confirm findOne(long confirmId) {
		return confirmRepository.findOne(confirmId);
	}
	
	public Confirm findOneByIdentificationKey(String signingKey) {
		return confirmRepository.findOneByIdentificationKey(signingKey);
	}
	
	public void sendMailforSignUp(Confirm confirm) throws FailedSendingEmailException {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "utf-8");
			String htmlMsg = "<h3>Welcome to the Singularity</h3>"
					+ "<a href='http://localhost:8080/user/confirm/" + confirm.getIdentificationKey() + "' style='font-size: 15px;"
					+ "color: white; text-decoration:none'>"
					+ "<div style='padding: 10px; border: 0px; width: 150px;"
					+ "margin: 15px 5px; background-color: red; "
					+ "text-align:center'>find Singularity for Me</div></a>"
					+ "<p>Copyright &copy; by hyvä. All rights reserved.</p>";
			
			messageHelper.setTo(confirm.getUserEmail());
			messageHelper.setFrom("hakimaru@naver.com","singularityfor.me");
			messageHelper.setSubject("환영합니다. " + confirm.getUserName() + "님! Singularity 가입 인증 메일입니다.");
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
