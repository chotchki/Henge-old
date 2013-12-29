package us.chotchki.springWeb.web.pojo;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

import org.hibernate.validator.constraints.NotEmpty;

import us.chotchki.springWeb.db.pojo.User;

public class Register {
	@Valid
	private User user = null;
	private String retypePassword = null;
	
	@NotEmpty(message="You must supply a token")
	private String token = null;
	
	@AssertTrue(message="The retyped password must match the password field")
	private boolean isValid() {
		return retypePassword.equals(user.getPassword());
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getRetypePassword() {
		return retypePassword;
	}
	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
