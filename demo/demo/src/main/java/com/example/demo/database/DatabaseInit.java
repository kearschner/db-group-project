package com.example.demo.database;

import com.example.demo.data.Section;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

import static com.example.demo.parsing.OasisParser.createHTMLDocFromFile;
import static com.example.demo.parsing.OasisParser.parseSectionsFromDocument;

@Service
public class DatabaseInit {

    @PostConstruct
    public void getSchema() {
    }

    public static void main(String[] args) throws IOException {



    }

    /*
    public static final SessionFactory sessionFactory = buildSessionFactory();
    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        }
        catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void InitializeFromSections(List<Section> sections) {
        Session session = sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();

        try {
            session.persist(sections);
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }*/

}
