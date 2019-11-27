package com.example.demo.board.service;
 
import java.util.List;
 
import javax.annotation.Resource;
 
import org.springframework.stereotype.Service;
 
import com.example.demo.board.domain.CommentVO;
import com.example.demo.board.mapper.CommentMapper;
 
@Service("com.example.demo.board.service.CommentService")
public class CommentService {
 
    @Resource(name="com.example.demo.board.mapper.CommentMapper")
    CommentMapper mCommentMapper;
    
    public List<CommentVO> commentListService(int bno) throws Exception{
        
        return mCommentMapper.commentList(bno);
    }
    
    public int commentInsertService(CommentVO comment) throws Exception{
        
        return mCommentMapper.commentInsert(comment);
    }
    
    public int commentUpdateService(CommentVO comment) throws Exception{
        
        return mCommentMapper.commentUpdate(comment);
    }
    
    public int commentDeleteService(int cno) throws Exception{
        
        return mCommentMapper.commentDelete(cno);
    }
}
