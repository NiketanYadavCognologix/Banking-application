package com.cognologix.bankingApplication.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer customerId;

	@NotEmpty(message = "Account holder name cannot blank")
	private String customerName;

	@NotNull(message = "Date of birth cannot blank")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

	@NotEmpty(message = "Adhar number cannot blank")
	@Size(min = 12, max = 12, message = "Adhar number should be 12 character")
	private String adharNumber;

	@NotEmpty(message = "PAN number cannot blank")
	private String panCardNumber;

	@Email(message = "Email id cannot blank")
	private String emailId;

	@NotEmpty(message = "Gender cannot blank")
	private String gender;

	@Override
	public String toString() {
		String messageBody = "CustomerId={0} | customerName={1} | dateOfBirth={2} | " +
				"adharNumber={3} | PanNumber={4} | EmailId={5} | gender={6}";
		return java.text.MessageFormat.format(messageBody,customerId, customerName,
				dateOfBirth, adharNumber,panCardNumber,emailId,gender);
	}

}
