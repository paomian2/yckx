package com.brother.yckx.model;
/*
 * 个人动态实体类(还学要改进),等后台数据
 * 
 * **/
public class PersonalNewsEntity {
	
	private String newsId;
	private UserEntity newsUserEntity;
    private String newsTime;
    private String newsImg[];
	private String newsTxt;
	private String newsTag;
	private String keyword[];
	private String praise;
	private CommentEntity commentEntity;
	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	public UserEntity getNewsUserEntity() {
		return newsUserEntity;
	}
	public void setNewsUserEntity(UserEntity newsUserEntity) {
		this.newsUserEntity = newsUserEntity;
	}
	public String getNewsTime() {
		return newsTime;
	}
	public void setNewsTime(String newsTime) {
		this.newsTime = newsTime;
	}
	public String[] getNewsImg() {
		return newsImg;
	}
	public void setNewsImg(String[] newsImg) {
		this.newsImg = newsImg;
	}
	public String getNewsTxt() {
		return newsTxt;
	}
	public void setNewsTxt(String newsTxt) {
		this.newsTxt = newsTxt;
	}
	public String getNewsTag() {
		return newsTag;
	}
	public void setNewsTag(String newsTag) {
		this.newsTag = newsTag;
	}
	public String[] getKeyword() {
		return keyword;
	}
	public void setKeyword(String[] keyword) {
		this.keyword = keyword;
	}
	public String getPraise() {
		return praise;
	}
	public void setPraise(String praise) {
		this.praise = praise;
	}
	public CommentEntity getCommentEntity() {
		return commentEntity;
	}
	public void setCommentEntity(CommentEntity commentEntity) {
		this.commentEntity = commentEntity;
	}
	public PersonalNewsEntity(String newsId, UserEntity newsUserEntity,
			String newsTime, String[] newsImg, String newsTxt, String newsTag,
			String[] keyword, String praise, CommentEntity commentEntity) {
		super();
		this.newsId = newsId;
		this.newsUserEntity = newsUserEntity;
		this.newsTime = newsTime;
		this.newsImg = newsImg;
		this.newsTxt = newsTxt;
		this.newsTag = newsTag;
		this.keyword = keyword;
		this.praise = praise;
		this.commentEntity = commentEntity;
	}
	
	
	
}
