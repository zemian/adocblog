//package com.zemian.adocblog.service;
//
//import com.zemian.adocblog.data.dao.Paging;
//import com.zemian.adocblog.data.dao.PagingList;
//import com.zemian.adocblog.data.domain.ContentPub;
//import com.zemian.adocblog.data.domain.Blog;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
///**
// * A Page is a Doc with type = PAGE.
// */
//@Service
//@Transactional
//public class PageService {
//    @Autowired
//    private DocService docService;
//
//    public void create(Blog doc) {
//        if (doc.getType() != Blog.Type.PAGE)
//            doc.setType(Blog.Type.PAGE);
//        docService.create(doc);
//    }
//
//    public Blog get(Integer id) {
//        return docService.get(id);
//    }
//
//    public void publish(Blog doc, ContentPub newPub) {
//        docService.publish(doc, newPub);
//    }
//
//    public boolean unpublish(Blog doc) {
//        return docService.unpublish(doc);
//    }
//
//    public void update(Blog doc) {
//        docService.update(doc);
//    }
//
//    public void markForDelete(Integer docId) {
//        docService.markForDelete(docId);
//    }
//
//    public PagingList<Blog> findLatest(Paging paging) {
//        return docService.findLatest(paging, Blog.Type.PAGE);
//    }
//
//    public PagingList<Blog> findHistories(Integer docId, Paging paging) {
//        return docService.findHistories(docId, paging, Blog.Type.PAGE);
//    }
//
//    public PagingList<Blog> findPublished(Paging paging) {
//        return docService.findPublished(paging, Blog.Type.PAGE);
//    }
//
//    public PagingList<Blog> searchPublished(Paging paging, String searchTerms) {
//        return docService.searchPublished(paging, Blog.Type.PAGE, searchTerms);
//    }
//
//    public int getPublishedCount() {
//        return docService.getPublishedCount(Blog.Type.PAGE);
//    }
//
//    public int getTotalCount() {
//        return docService.getTotalCount(Blog.Type.PAGE);
//    }
//
//    public List<Blog> findByPathNames(String... paths) {
//        return docService.findByPathNames(Blog.Type.PAGE, paths);
//    }
//}
