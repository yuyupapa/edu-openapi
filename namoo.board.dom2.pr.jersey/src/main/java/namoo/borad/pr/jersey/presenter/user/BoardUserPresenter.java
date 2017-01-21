/*
 * COPYRIGHT (c) Nextree Consulting 2015
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:jsseo@nextree.co.kr">Seo, Jisu</a>
 * @since 2015. 2. 10.
 */

package namoo.borad.pr.jersey.presenter.user;

import java.util.List;

import namoo.board.dom2.entity.user.BoardMember;
import namoo.board.dom2.entity.user.BoardTeam;
import namoo.board.dom2.entity.user.BoardUser;
import namoo.board.dom2.service.BoardUserService;
import namoo.board.dom2.util.exception.NamooBoardException;
import namoo.board.dom2.util.json.JsonUtil;
import namoo.borad.pr.jersey.request.JerseyPresentRequester;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource.Builder;

public class BoardUserPresenter implements BoardUserService{
	//
	private JerseyPresentRequester requester;
	
	public BoardUserPresenter() {
		//
		this.requester = new JerseyPresentRequester();
	}

	@Override
	public void addToBoardTeam(String teamUsid, List<String> userEmails) {
		//
		String url ="/users/boardMember/"+ teamUsid;
		Builder requestBuilder = requester.getRequestBuilder(url);
		
		ClientResponse response = requestBuilder.post(ClientResponse.class, JsonUtil.toJson(userEmails));
		
		if (response.getStatus() != Status.NO_CONTENT.getStatusCode()) {
			String resultJson = response.getEntity(String.class);
			NamooBoardException exception = JsonUtil.fromJson(resultJson, NamooBoardException.class);
			throw new NamooBoardException(exception.getMessage(), exception.getCode());
		}
	}
	
	@Override
	public List<BoardTeam> findAllBoardTeams() {
		//
		String url = "/users/boardTeam";
		
		Builder requestBuilder = requester.getRequestBuilder(url);
		ClientResponse response = requestBuilder.get(ClientResponse.class);
		
		String resultJson;
		if (response.getStatus() != Status.OK.getStatusCode()) {
			 resultJson = response.getEntity(String.class);
			NamooBoardException exception = JsonUtil.fromJson(resultJson, NamooBoardException.class);
			throw new NamooBoardException(exception.getMessage(), exception.getCode());
		}
		
		resultJson = response.getEntity(String.class);
		return JsonUtil.fromJsonArray(resultJson, BoardTeam[].class);
	}
	
	@Override
	public void removeUserWithEmail(String userEmail) {
		//
		String url = "/users/"+userEmail;
		
		Builder requestBuilder = requester.getRequestBuilder(url);
		ClientResponse response = requestBuilder.delete(ClientResponse.class);
		
		if (response.getStatus() != Status.NO_CONTENT.getStatusCode()) {
			String resultJson = response.getEntity(String.class);
			NamooBoardException exception = JsonUtil.fromJson(resultJson, NamooBoardException.class);
			throw new NamooBoardException(exception.getMessage(), exception.getCode());
		}
	}
	
	@Override
	public List<BoardUser> findAllUsers() throws NamooBoardException {
		//
		String url ="/users";
		
		Builder requestBuilder = requester.getRequestBuilder(url);
		
		ClientResponse response = requestBuilder.get(ClientResponse.class);
		
		String resultJson;
		if (response.getStatus() != Status.OK.getStatusCode()) {
			 resultJson = response.getEntity(String.class);
			NamooBoardException exception = JsonUtil.fromJson(resultJson, NamooBoardException.class);
			throw new NamooBoardException(exception.getMessage(), exception.getCode());
		}
		
		resultJson = response.getEntity(String.class);
		return JsonUtil.fromJsonArray(resultJson, BoardUser[].class);
	}
	
	@Override
	public List<BoardMember> findTeamBoardMembers(String teamUsid) {
		//
		String url ="/users/boardMember/"+teamUsid;		
		Builder requestBuilder = requester.getRequestBuilder(url);
		ClientResponse response = requestBuilder.get(ClientResponse.class);
		
		String resultJson;
		if (response.getStatus() != Status.OK.getStatusCode()) {
			resultJson = response.getEntity(String.class);
			NamooBoardException exception = JsonUtil.fromJson(resultJson, NamooBoardException.class);
			throw new NamooBoardException(exception.getMessage(), exception.getCode());
		}
		
		resultJson = response.getEntity(String.class);
		return JsonUtil.fromJsonArray(resultJson, BoardMember[].class);
	}
	
	@Override
	public BoardUser findUserWithEmail(String userEmail) {
		//
		String url ="/users/"+userEmail;
		
		Builder requestBuilder = requester.getRequestBuilder(url);
		
		ClientResponse response = requestBuilder.get(ClientResponse.class);
		
		String resultJson;
		if (response.getStatus() != Status.OK.getStatusCode()) {
			resultJson = response.getEntity(String.class);
			NamooBoardException exception = JsonUtil.fromJson(resultJson, NamooBoardException.class);
			throw new NamooBoardException(exception.getMessage(), exception.getCode());
		}
		
		resultJson = response.getEntity(String.class);
		return (BoardUser) JsonUtil.fromJson(resultJson, BoardUser.class);
	}
	
	@Override
	public boolean loginAsUser(String userEmail, String password) {
		// 
		String url ="/users/login";
		String param = "userEmail=" + userEmail + "&password=" +password;
		Builder requestBuilder = requester.getRequestBuilder(url);
		
		ClientResponse response = requestBuilder.post(ClientResponse.class, param);
		
		String resultJson;
		if (response.getStatus() != Status.OK.getStatusCode()) {
			resultJson = response.getEntity(String.class);
			NamooBoardException exception = JsonUtil.fromJson(resultJson, NamooBoardException.class);
			throw new NamooBoardException(exception.getMessage(), exception.getCode());
		}
		
		resultJson = response.getEntity(String.class);
		BoardUser user = (BoardUser) JsonUtil.fromJson(resultJson, BoardUser.class);
		if(user == null)
			return false;
		else
			return true;
	}
	
	@Override
	public String registerBoardTeam(String teamName, String adminEmail) {
		//
		String url ="/users/boardTeam";
		String param = "teamName=" + teamName + "&adminEmail=" +adminEmail;
		Builder requestBuilder = requester.getRequestBuilder(url);
		
		ClientResponse response = requestBuilder.post(ClientResponse. class, param);
		
		String resultJson;
		if (response.getStatus() != Status.OK.getStatusCode()) {
			resultJson = response.getEntity(String.class);
			NamooBoardException exception = JsonUtil.fromJson(resultJson, NamooBoardException.class);
			throw new NamooBoardException(exception.getMessage(), exception.getCode());
		}
		
		resultJson = response.getEntity(String.class);
		return resultJson;
	}
	
	@Override
	public void registerUser(BoardUser user) {
		//
		String url ="/users";
		Builder requestBuilder = requester.getRequestBuilder(url);
		
		ClientResponse response = requestBuilder.post(ClientResponse. class, JsonUtil.toJson(user));
		
		if (response.getStatus() != Status.NO_CONTENT.getStatusCode()) {
			String resultJson = response.getEntity(String.class);
			NamooBoardException exception = JsonUtil.fromJson(resultJson, NamooBoardException.class);
			throw new NamooBoardException(exception.getMessage(), exception.getCode());
		}		
	};
	
	@Override
	public void removeBoardTeam(String teamUsid) {
		// 
		String url ="/users/boardTeam/"+teamUsid;
		Builder requestBuilder = requester.getRequestBuilder(url);
		
		ClientResponse response = requestBuilder.delete(ClientResponse. class);
		
		if (response.getStatus() != Status.NO_CONTENT.getStatusCode()) {
			String resultJson = response.getEntity(String.class);
			NamooBoardException exception = JsonUtil.fromJson(resultJson, NamooBoardException.class);
			throw new NamooBoardException(exception.getMessage(), exception.getCode());
		}
	}
	
	@Override
	public void removeFromBoardTeam(String teamUsid, String userEmail) {
		// 
		String url ="/users/boardMember/"+teamUsid+"/"+userEmail;
		Builder requestBuilder = requester.getRequestBuilder(url);
		
		ClientResponse response = requestBuilder.delete(ClientResponse. class);
		
		if (response.getStatus() != Status.NO_CONTENT.getStatusCode()) {
			String resultJson = response.getEntity(String.class);
			NamooBoardException exception = JsonUtil.fromJson(resultJson, NamooBoardException.class);
			throw new NamooBoardException(exception.getMessage(), exception.getCode());
		}
	}
}