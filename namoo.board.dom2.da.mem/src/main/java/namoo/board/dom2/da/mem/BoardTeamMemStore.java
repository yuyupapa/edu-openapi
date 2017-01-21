/*
 * COPYRIGHT (c) Nextree Consulting 2015
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:hkkang@nextree.co.kr">Kang Hyoungkoo</a>
 * @since 2015. 1. 15.
 */
package namoo.board.dom2.da.mem;

import java.util.List;

import namoo.board.dom2.da.mem.mapstore.BoardTeamRepository;
import namoo.board.dom2.da.mem.mapstore.UsidSeqRepository;
import namoo.board.dom2.entity.user.BoardMember;
import namoo.board.dom2.entity.user.BoardTeam;
import namoo.board.dom2.store.BoardTeamStore;
import namoo.board.dom2.util.exception.EmptyResultException;

public class BoardTeamMemStore implements BoardTeamStore {
    //
    private BoardTeamRepository teamStore = BoardTeamRepository.getInstance();
    private UsidSeqRepository seqStore = UsidSeqRepository.getInstance();
    
    //--------------------------------------------------------------------------
    
    @Override
    public void create(BoardTeam team) {
        // 
        teamStore.put(team);
    }

    @Override
    public BoardTeam retrieve(String usid) throws EmptyResultException {
        // 
        BoardTeam team = teamStore.get(usid); 
        if (team == null) {
            throw new EmptyResultException("No such a team --> " + usid);
        }
        
        return team;
    }

    @Override
    public BoardTeam retrieveByName(String name) throws EmptyResultException {
        // 
        BoardTeam team = teamStore.getByName(name); 
        if (team == null) {
            throw new EmptyResultException("No such a team --> " + name);
        }
        
        return team;
    }

    @Override
    public List<BoardTeam> retrieveAll() {
        // 
        return teamStore.getAll();
    }

    @Override
    public void delete(String usid) {
        // 
        teamStore.remove(usid);
    }

    @Override
    public int nextSequence() {
        // 
        return seqStore.nextTeamSequence();
    }
    
    @Override
    public boolean isExist(String usid) {
        // 
        return teamStore.get(usid) != null;
    }

    @Override
    public boolean isExistByName(String name) {
        // 
        return teamStore.getByName(name) != null;
    }
    

    //--------------------------------------------------------------------------
    // team member
    
    @Override
    public void createMember(String teamUsid, BoardMember member) {
        // 
        teamStore.putMember(teamUsid, member);
    }

    @Override
    public BoardMember retrieveMember(String teamUsid, String memberEmail) throws EmptyResultException {
        // 
        BoardMember member = teamStore.getMember(teamUsid, memberEmail);
        
        if (member == null) {
            throw new EmptyResultException("No such a member --> teamUsid: " + memberEmail + ", email: " + memberEmail);
        }
        return member;
    }

    @Override
    public List<BoardMember> retrieveMembers(String teamUsid) {
        // 
        return teamStore.getMembers(teamUsid);
    }

    @Override
    public void deleteMember(String teamUsid, String memberEmail) {
        // 
        teamStore.removeMember(teamUsid, memberEmail);
    }

    @Override
    public boolean isExistMember(String teamUsid, String memberEmail) {
        // 
        return teamStore.getMember(teamUsid, memberEmail) != null;
    }
    

}
