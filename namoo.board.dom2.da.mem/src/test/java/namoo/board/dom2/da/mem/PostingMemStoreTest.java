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

import namoo.board.dom2.entity.board.Posting;
import namoo.board.dom2.entity.board.PostingComment;
import namoo.board.dom2.entity.board.PostingContents;
import namoo.board.dom2.entity.board.PostingOption;
import namoo.board.dom2.entity.board.SocialBoard;
import namoo.board.dom2.entity.user.BoardMember;
import namoo.board.dom2.entity.user.BoardTeam;
import namoo.board.dom2.entity.user.BoardUser;
import namoo.board.dom2.store.BoardTeamStore;
import namoo.board.dom2.store.BoardUserStore;
import namoo.board.dom2.store.PostingStore;
import namoo.board.dom2.store.SocialBoardStore;
import namoo.board.dom2.util.exception.EmptyResultException;
import namoo.board.dom2.util.page.Page;
import namoo.board.dom2.util.page.PageCriteria;

public class PostingMemStoreTest {
    //
    private PostingStore postingStore = new PostingMemStore();
    private BoardUserStore userStore = new BoardUserMemStore();
    private BoardTeamStore teamStore = new BoardTeamMemStore();
    private SocialBoardStore boardStore = new SocialBoardMemStore();
    
    private final String postingUsid = "001-0001";
    
    @Before
    public void setUp() {
        // 
        // DCBoardUser 데이터 생성
        List<BoardUser> users = new ArrayList<BoardUser>();
        
        users.add(new BoardUser("tsong@nextree.co.kr", "송태국", "010-1111-2222"));
        users.add(new BoardUser("hkkang@nextree.co.kr", "강형구", "010-1234-5678"));
        users.add(new BoardUser("syhan@nextree.co.kr", "한성영", "010-0000-0000"));
        users.add(new BoardUser("eschoi@nextree.co.kr", "최은선", "010-2222-2222"));
        
        for (BoardUser user : users) {
            userStore.create(user);
        }

        // DCBoardTeam 데이터 생성
        BoardTeam team = new BoardTeam("컨설팅팀", new BoardUser("tsong@nextree.co.kr"));
        team.setUsid("001");
        
        teamStore.create(team);
        teamStore.nextSequence();
        
        // DCBoardMember 데이터 생성
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
        
        // DCSocialBoard 데이터 생성
        String registTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);  
        SocialBoard board = new SocialBoard("001", "영화게시판", registTime, "001", true);
        boardStore.create(board);
        boardStore.nextSequence();
        
        board = new SocialBoard("002", "일정게시판", registTime, "001", false);
        boardStore.create(board);
        boardStore.nextSequence();
        
        // DCPosting 데이터 생성
        Posting posting = new Posting("001", "금주 회식 공지", new BoardUser("hkkang@nextree.co.kr"));
        posting.setUsid("001-0001");
        
        PostingOption option = new PostingOption().commentable(true).anonymousComment(false);
        posting.setOption(option);
        posting.setContents(new PostingContents(posting, "팀 회식이 금주 금요일 7시에 있습니다. 장소는 추후 공지하도록 하겠습니다. 많은 참석 바랍니다."));
        
        postingStore.create(posting);
        postingStore.nextSequence("001");
        
        
        
        posting = new Posting("001", "1월 휴가계획일을 체크합니다.", new BoardUser("hkkang@nextree.co.kr"));
        posting.setUsid("001-0002");
        
        option = new PostingOption().commentable(true).anonymousComment(true);
        posting.setOption(option);
        posting.setContents(new PostingContents(posting, "1월 휴가일정을 체크하려 합니다. 개인 별 휴가계획 일자를 담당자에게 메일로 알려주시기 바랍니다."));
        
        postingStore.create(posting);
        postingStore.nextSequence("001");
        
        // DCPostingComment 데이터 생성
        PostingComment comment = new PostingComment("네 알겠습니다.", "syhan@nextree.co.kr");
        comment.setSequence(1);
        
        postingStore.createComment("001-0001", comment);
        postingStore.nextCommentSequence("001-0001");
        
        
        
        comment = new PostingComment("참석하도록 하겠습니다.", "eschoi@nextree.co.kr");
        comment.setSequence(2);
        
        postingStore.createComment("001-0001", comment);
        postingStore.nextCommentSequence("001-0001");
    }
    
    @After
    public void tearDown() {
        //
        // DCSocialBoard, DCPosting 데이터 삭제
        List<SocialBoard> allBoards = boardStore.retrieveAll();
        
        for (SocialBoard board : allBoards) {
            //
            boardStore.delete(board.getUsid());
            postingStore.deleteByBoard(board.getUsid());
        }
        
        
        // DCBoardTeam, DCBoardMember 데이터 삭제
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
        
        // DCBoardUser 데이터 삭제
        List<BoardUser> allUsers = userStore.retrieveAll();
        
        for (BoardUser user : allUsers) {
            userStore.deleteByEmail(user.getEmail());
        }
    }
    
    //--------------------------------------------------------------------------
    // posting
    
    @Test
    public void testCreate() throws EmptyResultException {
        //
        BoardUser user = userStore.retrieveByEmail("hkkang@nextree.co.kr");
        Posting posting = new Posting("001", "팀별 일정 집계 중입니다.", user);
        posting.setUsid("001-0003");
        
        postingStore.create(posting);
        
        Posting created = postingStore.retrieve("001-0003");
        
        assertNotNull(created);
        assertEquals(posting.getUsid(), created.getUsid());
        assertEquals(posting.getTitle(), created.getTitle());
        assertEquals(posting.getWriterEmail(), created.getWriterEmail());
        assertEquals(posting.getWriterName(), created.getWriterName());
        assertEquals(posting.getReadCount(), created.getReadCount());
        assertEquals(posting.getBoardUsid(), created.getBoardUsid());
        assertEquals(posting.getRegistTime(), created.getRegistTime());
        assertEquals(posting.getOption().isCommentable(), created.getOption().isCommentable());
        assertEquals(posting.getOption().isAnonymousComment(), created.getOption().isAnonymousComment());
    }
    
    @Test
    public void testRetrieve() throws EmptyResultException {
        //
        Posting posting = postingStore.retrieve(this.postingUsid);
        
        assertNotNull(posting);
        assertEquals("금주 회식 공지", posting.getTitle());
        assertEquals("hkkang@nextree.co.kr", posting.getWriterEmail());
        // TODO : 데이터 조회시 연관된 데이터 같이 조회 혹은 수정시 모두 처리 
//        assertEquals("강형구", posting.getWriterName());
        assertEquals(0, posting.getReadCount());
        assertEquals("001", posting.getBoardUsid());
        assertEquals(true, posting.getOption().isCommentable());
        assertEquals(false, posting.getOption().isAnonymousComment());
    }
    
    @Test
    public void testRetrievePage() {
        //
        PageCriteria criteria = new PageCriteria(2, 1);
        Page<Posting> postingPage = postingStore.retrievePage("001", criteria);
        
        assertEquals(1, postingPage.getResults().size());
        assertEquals(2, postingPage.getTotalItemCount());
    }
    
    @Test
    public void testUpdate() throws EmptyResultException {
        //
        Posting posting = postingStore.retrieve(this.postingUsid);
        
        posting.setTitle("회식일자가 변경되었습니다.");
        posting.getOption().setCommentable(false);
        posting.getOption().setAnonymousComment(true);
        
        postingStore.update(posting);
        
        Posting updated = postingStore.retrieve(this.postingUsid);
        assertEquals(posting.getTitle(), updated.getTitle());
        assertEquals(posting.getOption().isCommentable(), updated.getOption().isCommentable());
        assertEquals(posting.getOption().isAnonymousComment(), updated.getOption().isAnonymousComment());
    }
    
    @Test
    public void testDelete() throws EmptyResultException {
        //
        Posting posting = postingStore.retrieve(this.postingUsid);
        assertNotNull(posting);
        
        postingStore.delete(this.postingUsid);
        
        try {
            postingStore.retrieve(this.postingUsid);
            fail("Didn't throw exception");
        } catch (EmptyResultException e) {}
    }

    @Test
    public void testDeleteByBoard() throws EmptyResultException {
        //
        Posting posting = postingStore.retrieve(this.postingUsid);
        assertNotNull(posting);
        
        postingStore.deleteByBoard("001");
        
        try {
            postingStore.retrieve(this.postingUsid);
            fail("Didn't throw exception");
        } catch (EmptyResultException e) {}
        
        try {
            postingStore.retrieveComment(this.postingUsid, 1);
            fail("Didn't throw exception");
        } catch (EmptyResultException e) {}
        
    }
    
    @Test
    public void testNextSequence() {
        //
        int nextSequence = postingStore.nextSequence("001");
        assertTrue(nextSequence >= 3);
        
        nextSequence = postingStore.nextSequence("002");
        assertEquals(1, nextSequence);
    }
    
    @Test
    public void testIncreaseReadCount() throws EmptyResultException {
        //
        postingStore.increaseReadCount(this.postingUsid);
        
        Posting posting = postingStore.retrieve(this.postingUsid);
        assertEquals(1, posting.getReadCount());
    }

    //--------------------------------------------------------------------------
    // contents
    
    @Test
    public void testCreateContents() throws EmptyResultException {
        //
        Posting posting = postingStore.retrieve(this.postingUsid);
        String contentsStr = "안녕하세요. 반갑습니다.";
        PostingContents contents = new PostingContents(posting, contentsStr);
        
        postingStore.createContents(contents);
        
        PostingContents createdContents = postingStore.retrieveContents(this.postingUsid);
        assertEquals(contents.getContents(), createdContents.getContents());
    }
    
    @Test
    public void testRetrieveContents() throws EmptyResultException {
        //
        PostingContents contents = postingStore.retrieveContents(this.postingUsid);
        
        assertNotNull(contents);
        assertEquals("팀 회식이 금주 금요일 7시에 있습니다. 장소는 추후 공지하도록 하겠습니다. 많은 참석 바랍니다.", contents.getContents());
        
        List<PostingComment> comments = contents.getCommentBag().getComments();
        assertEquals(2, comments.size());
    }
    
    @Test
    public void testUpdateContents() throws EmptyResultException {
        //
        Posting posting = postingStore.retrieve(this.postingUsid);
        String contentsStr = "안녕하세요. 반갑습니다.";
        PostingContents contents = new PostingContents(posting, contentsStr);
        
        postingStore.updateContents(contents);
        
        PostingContents updatedContents = postingStore.retrieveContents(this.postingUsid);
        assertEquals(contents.getContents(), updatedContents.getContents());
    }
    
    @Test
    public void testCreateComment() throws EmptyResultException {
        //
        PostingComment comment = new PostingComment("저도 참석하겠습니다. 즐거운 회식 기대합니다!.", "hkkang@nextree.co.kr");
        comment.setSequence(3);
        
        postingStore.createComment(this.postingUsid, comment);
        
        PostingContents contents = postingStore.retrieveContents(this.postingUsid);
        assertEquals(3, contents.getCommentBag().getComments().size());
    }
    
    @Test
    public void testRetrieveComment() throws EmptyResultException {
        //
        PostingComment comment = postingStore.retrieveComment(this.postingUsid, 1);
        
        assertNotNull(comment);
        assertEquals(1, comment.getSequence());
        assertEquals("네 알겠습니다.", comment.getComment());
        assertEquals("syhan@nextree.co.kr", comment.getWriterEmail());
    }
    
    @Test
    public void testDeleteComment() throws EmptyResultException {
        //
        postingStore.deleteComment(this.postingUsid, 1);
        
        try {
            postingStore.retrieveComment(this.postingUsid, 1);
            fail("Didn't throw exception");
        } catch (EmptyResultException e) {}
        
        PostingContents contents = postingStore.retrieveContents(this.postingUsid);
        assertEquals(1, contents.getCommentBag().getComments().size());
    }

    @Test
    public void testNextCommentSequence() {
        //
        int nextCommentSequence = postingStore.nextCommentSequence("001-0001");
        assertTrue(nextCommentSequence >= 3);
        
        nextCommentSequence = postingStore.nextCommentSequence("001-0002");
        assertTrue(nextCommentSequence == 1);
    }
}
