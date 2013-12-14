package com.arisprung.tailgate.facebook;

public class FacebookUser
{

	private String userName;
	private String idUser;
	private String email;
	// private String birthday;
	private String location;

	public FacebookUser()
	{

	}

	public FacebookUser(String userName, String idUser, String email, String location)
	{
		super();
		this.userName = userName;
		this.idUser = idUser;
		this.email = email;

		this.location = location;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getIdUser()
	{
		return idUser;
	}

	public void setIdUser(String idUser)
	{
		this.idUser = idUser;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

}
