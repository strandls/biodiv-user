/**
 * 
 */
package com.strandls.user.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Abhishek Rudra
 *
 */

@Entity
@Table(name = "follow")
public class Follow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8304116269803899781L;

	private Long id;
	private Long version;
	private Long objectId;
	private String objectType;
	private Long authorId;
	private Date createdOn;

	/**
	 * 
	 */
	public Follow() {
		super();
	}

	/**
	 * @param id
	 * @param version
	 * @param objectId
	 * @param objectType
	 * @param authorId
	 * @param createdOn
	 */
	public Follow(Long id, Long version, Long objectId, String objectType, Long authorId, Date createdOn) {
		super();
		this.id = id;
		this.version = version;
		this.objectId = objectId;
		this.objectType = objectType;
		this.authorId = authorId;
		this.createdOn = createdOn;
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "version")
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Column(name = "object_id")
	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	@Column(name = "object_type")
	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	@Column(name = "author_id")
	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	@Column(name = "created_on")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
