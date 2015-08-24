package singularity.notification.domain;

import lombok.Data;
import singularity.party.domain.Party;
import singularity.user.domain.User;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Notification {
	
	public enum Pattern {
		REQUEST, INVITE, REPLY, NEW_POST
	}
	
	private enum Status {
		ON, OFF
	}

    @Id
    @GeneratedValue
	private Long Id;

    @Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "writer_FK")
	private User writer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reader_FK")
	private User reader;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "party_FK")
	private Party party;

    @Column
	private Pattern pattern;

    @Column
	private Status status;
    
    public Notification(Date createDate, User writer, User reader, Party party, Pattern pattern) {
    	this.createDate = createDate;
    	this.writer = writer;
    	this.reader = reader;
    	this.party = party;
    	this.pattern = pattern;
    	this.status = Status.ON;
    }
    
    public boolean isRequest() {
    	return (this.pattern.equals(Pattern.REQUEST) && this.status.equals(Status.ON)); 
    }
	
	
}
