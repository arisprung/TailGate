package com.arisprung.tailgate;

public class MessageBean
{
	private long id;
	private String userName;
	private long date;
	private String faceID;
	private String message;

	public MessageBean()
	{

	}

	public MessageBean(String userName, long date, String faceID, String message)
	{
		super();
		this.userName = userName;
		this.date = date;
		this.faceID = faceID;
		this.message = message;
	}

	public MessageBean(String faceID, String message, String userName)
	{
		super();
		this.userName = userName;

		this.faceID = faceID;
		this.message = message;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public long getDate()
	{
		return date;
	}

	public void setDate(long date)
	{
		this.date = date;
	}

	public String getFaceID()
	{
		return faceID;
	}

	public void setFaceID(String faceID)
	{
		this.faceID = faceID;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

}
