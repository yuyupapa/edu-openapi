/*
 * COPYRIGHT (c) Nextree Consulting 2015
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:hkkang@nextree.co.kr">Kang Hyoungkoo</a>
 * @since 2015. 1. 5.
 */

package namoo.board.dom2.da.mem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import namoo.board.dom2.entity.board.SocialBoard;
import namoo.board.dom2.entity.user.BoardMember;
import namoo.board.dom2.entity.user.BoardTeam;
import namoo.board.dom2.entity.user.BoardUser;
import namoo.board.dom2.store.BoardTeamStore;
import namoo.board.dom2.store.BoardUserStore;
import namoo.board.dom2.store.SocialBoardStore;
import namoo.board.dom2.util.exception.EmptyResultException;

public class SocialBoardMemStoreTest {
    //
    private SocialBoardStore boardStore = new SocialBoardMemStore();
    private BoardTeamStore teamStore = new BoardTeamMemStore();
    private BoardUserStore userStore = new BoardUserMemStore();
    
    private final String boardUsid = "001";
    
    @Before
    public void setUp() {
        // 
        // 사용자 데이터 생성
        List<BoardUser> users = new ArrayList<BoardUser>();
        
        users.add(new BoardUser("tsong@nextree.co.kr", "송태국", "010-1111-2222"));
        users.add(new BoardUser("hkkang@nextree.co.kr", "강형구", "010-1234-5678"));
        users.add(new BoardUser("syhan@nextree.co.kr", "한성영", "010-0000-0000"));
        users.add(new BoardUser("eschoi@nextree.co.kr", "최은선", "010-2222-2222"));
        
        for (BoardUser user : users) {
            userStore.create(user);
        }

        // 팀 데이터 생성
        BoardTeam team = new BoardTeam("컨설팅팀", new BoardUser("tsong@nextree.co.kr"));
        team.setUsid("001");
        
        teamStore.create(team);
        teamStore.nextSequence();
        
        // 팀 멤버 데이터 생성
        List<BoardMember> members = new ArrayList<BoardMember>();
        
        BoardMember member = new BoardMember("001-hkkang@nextree.co.kr");
        member.setUser(new BoardUser("hkkang@nextree.co.kr"));
        members.add(member);
        
        member = new BoardMember("001-syhan@nextree.co.kr");
        member.setUser(new BoardUser("syhan@nextree.co.kr"));
        members.add(member);
        
        for (BoardMember boardMember : members) {
            teamStore.createMember("001", boardMember);
        }
        
        // 게시판 데이터 생성
        String registTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);  
        SocialBoard board = new SocialBoard("001", "영화게시판", registTime, "001", true);
        boardStore.create(board);
        boardStore.nextSequence();
        
        board = new SocialBoard("002", "일정게시판", registTime, "001", false);
        boardStore.create(board);
        boardStore.nextSequence();
    }
    
    @After
    public void tearDown() {
        //
        // 게시판 데이터 삭제
        List<SocialBoard> allBoards = boardStore.retrieveAll();
        
        for (SocialBoard socialBoard : allBoards) {
            //
            boardStore.delete(socialBoard.getUsid());
        }
        
        
        // 팀, 멤버 데이터 삭제
        List<BoardTeam> allTeams = teamStore.retrieveAll();
        
        for (BoardTeam team : allTeams) {
            //
            List<BoardMember> teamMembers = teamStore.retrieveMembers(team.getUsid());
            for (BoardMember member : teamMembers) {
                //
                teamStore.deleteMember(team.getUsid(), member.getUser().getEmail());
            }
            teamStore.delete(team.getUsid());
        }
        
        // 사용자 데이터 삭제
        List<BoardUser> allUsers = userStore.retrieveAll();
        
        for (BoardUser user : allUsers) {
            userStore.deleteByEmail(user.getEmail());
        }
    }
    
    //--------------------------------------------------------------------------
    
    @Test
    public void testCreate() throws EmptyResultException {
        //
        BoardTeam team = teamStore.retrieveByName("컨설팅팀");
        SocialBoard board = new SocialBoard(team, "스터디게시판");
        
        board.setUsid("003");
        
        String createdId = boardStore.create(board);
        
        SocialBoard created = boardStore.retrieve(createdId);
        assertNotNull(created);
        assertEquals(board.getName(), created.getName());
        assertEquals(true, board.isCommentable());
        assertEquals(board.getTeamUsid(), created.getTeamUsid());
    }
    
    @Test
    public void testRetrieve() throws EmptyResultException {
        //
        SocialBoard board = boardStore.retrieve(this.boardUsid);
        assertNotNull(board);
        assertEquals("영화게시판", board.getName());
        assertEquals(true, board.isCommentable());
        assertEquals("001", board.getTeamUsid());
    }
    
    @Test
    public void testRetrieveByName() throws EmptyResultException {
        //
        SocialBoard board = boardStore.retrieveByName("영화게시판");
        assertNotNull(board);
        assertEquals(this.boardUsid, board.getUsid());
        assertEquals(true, board.isCommentable());
        assertEquals("001", board.getTeamUsid());
    }
    
    @Test
    public void testRetrieveAll() {
        //
        List<SocialBoard> allBoards = boardStore.retrieveAll();
        
        assertEquals(allBoards.size(), 2);
    }
    
    @Test
    public void testUpdate() throws EmptyResultException {
        //
        SocialBoard board = boardStore.retrieve(this.boardUsid);
        board.setName("팁게시판");
        board.setCommentable(false);
        
        boardStore.update(board);
        
        
        SocialBoard updated = boardStore.retrieve(this.boardUsid);
        assertEquals(board.getName(), updated.getName());
        assertEquals(board.isCommentable(), updated.isCommentable());
    }
    
    @Test
    public void testDelete() throws EmptyResultException {
        //
        SocialBoard board = boardStore.retrieve(this.boardUsid);
        assertNotNull(board);
        
        boardStore.delete(this.boardUsid);
        
        try {
            boardStore.retrieve(this.boardUsid);
            fail("Didn't throw exception");
        } catch (EmptyResultException e) {}
    }
    
    @Test
    public void testNextSequence() {
        //
        int nextSequence = boardStore.nextSequence();
        assertTrue(nextSequence >= 3);
    }
}
