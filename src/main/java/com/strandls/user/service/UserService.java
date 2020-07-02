/**
 * 
 */
package com.strandls.user.service;

import java.util.List;

import com.strandls.user.pojo.FirebaseTokens;
import com.strandls.user.pojo.Follow;
import com.strandls.user.pojo.GroupAddMember;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserGroupMemberRole;
import com.strandls.user.pojo.UserGroupMembersCount;
import com.strandls.user.pojo.UserIbp;
import com.strandls.user.pojo.UserPermissions;

/**
 * @author Abhishek Rudra
 *
 */
public interface UserService {

	public User fetchUser(Long userId);

	public UserIbp fetchUserIbp(Long userId);

	public List<UserIbp> fetchUserIbpBulk(List<Long> userIds);

	public User getUserByEmailOrMobile(String data);

	public User getUserByEmail(String userEmail);

	public User getUserByMobile(String mobileNumber);

	public User updateUser(User user);

	public UserPermissions getUserPermissions(Long userId, String type, Long objectId);

	public Follow fetchByFollowId(Long id);

	public List<User> fetchRecipients(String objectType, Long objectId);

	public Follow fetchByFollowObject(String objectType, Long objectId, Long authorId);

	public List<Follow> fetchFollowByUser(Long authorId);

	public Follow updateFollow(String objectType, Long objectId, Long userId);

	public Follow unFollow(String type, Long objectId, Long userId);

	public Boolean checkUserGroupMember(Long userId, Long userGroupId);

	public List<User> getNames(String name);

	public FirebaseTokens saveToken(Long userId, String token);

	public List<UserGroupMembersCount> getUserGroupMemberCount();

	public Boolean checkFounderRole(Long userId, Long userGroupId);

	public Boolean checkModeratorRole(Long userId, Long userGroupId);

	public UserGroupMemberRole addMemberUG(Long userId, Long roleId, Long userGroupId);

	public Boolean removeGroupMember(Long userId, Long userGroupId);

	public Boolean joinGroup(Long userId, Long userGroupId);

	public List<Long> addMemberDirectly(GroupAddMember addMember);

	public List<User> getFounderModerator(Long userGroupId);

	public List<UserIbp> getFounderList(Long userGroupId);

	public List<UserIbp> getModeratorList(Long userGroupId);
}
