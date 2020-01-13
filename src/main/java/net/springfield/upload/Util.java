package net.springfield.upload;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

public class Util {
	private static MongoClient getConnection() {
		        int port_no = 27017;
		        String url = "localhost";	 
		        MongoClient mongoClntObj = new MongoClient(url, port_no);
		        return mongoClntObj;
		    }
	public static MongoDatabase getDb(String databaseName) {
		
		MongoDatabase db = getConnection().getDatabase(databaseName);
		return db;
		
	}
	public static ArrayList<String> getQueries(String databaseName,String collectionName,String queryName,String FileName)
	{
		ArrayList<String> result = new ArrayList<String>();
		MongoDatabase db = getConnection().getDatabase(databaseName);
		MongoCollection<Document> collection = db.getCollection(collectionName);
		 List obj = new ArrayList();
		 //obj.add(new BasicDBObject("", user));
		 obj.add(new BasicDBObject("FileName", FileName));
		 BasicDBObject whereQuery = new BasicDBObject();
		 whereQuery.put("$and", obj);
		 FindIterable<Document> cursor = collection.find(whereQuery);
		    for(Document doc : cursor) {
		    	            System.out.println("Found?= " + doc);
		    	            result.add(doc.getString(queryName));
		    	            //getServletContext().getRequestDispatcher("/upload.jsp").forward(request, response);
		    	        }

		    return result;
	}
}
