/*
 * COPYRIGHT (c) Nextree Consulting 2015
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:hkkang@nextree.co.kr">Kang Hyoungkoo</a>
 * @since 2015. 1. 15.
 */
package namoo.board.dom2.da.mem.mapstore;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import namoo.board.dom2.da.mem.util.ObjectCopyUtil;
import namoo.board.dom2.entity.user.BoardMember;
import namoo.board.dom2.entity.user.BoardTeam;

public class BoardTeamRepository {
    //
    private static BoardTeamRepository instance = new BoardTeamRepository();
    private Map<String, BoardTeam> teamMap = new HashMap<String, BoardTeam>();

    
    private BoardTeamRepository() {}
    
    public static BoardTeamRepository getInstance() {
        //
        return instance;
    }
    
    //--------------------------------------------------------------------------
    // board team
    
    public void put(BoardTeam team) {
        //
        BoardTeam copied = ObjectCopyUtil.copyObject(team, BoardTeam.class); 
        this.teamMap.put(copied.getUsid(), copied);
    }
    
    public BoardTeam get(String usid) {
        //
        BoardTeam team = this.teamMap.get(usid); 
        return ObjectCopyUtil.copyObject(team, BoardTeam.class);
    }
    
    public BoardTeam getByName(String name) {
        //
        for (BoardTeam team : this.teamMap.values()) {
            if (team.getName().equals(name)) {
                return ObjectCopyUtil.copyObject(team, BoardTeam.class);
            }
        }
        return null;
    }
    
    public List<BoardTeam> getAll() {
        //
        return ObjectCopyUtil.copyObjects(this.teamMap.values(), BoardTeam.class);
    }
    
    public void remove(String usid) {
        //
        this.teamMap.remove(usid);
    }
    
    
    //--------------------------------------------------------------------------
    // team member

    public void putMember(String teamUsid, BoardMember member) {
        //
        BoardTeam team = this.teamMap.get(teamUsid);
        team.addMember(ObjectCopyUtil.copyObject(member, BoardMember.class));
    }
    
    public BoardMember getMember(String teamUsid, String memberEmail) {
        //
        BoardTeam team = this.teamMap.get(teamUsid);
        return ObjectCopyUtil.copyObject(team.getMember(memberEmail), BoardMember.class);
    }
    
    public List<BoardMember> getMembers(String teamUsid) {
        //
        BoardTeam team = this.teamMap.get(teamUsid);
        if (team == null) {
            return Collections.emptyList();
        }
        return ObjectCopyUtil.copyObjects(team.getMembers(), BoardMember.class);
    }
    
    public void removeMember(String teamUsid, String memberEmail) {
        //
        BoardTeam team = this.teamMap.get(teamUsid);
        team.removeMember(memberEmail);
    }
    
}
