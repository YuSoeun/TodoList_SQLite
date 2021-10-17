import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello World!!");
		
		Connection con = null;
		
		try {
			// check SQLite JDBC
			Class.forName("org.sqlite.JDBC");
			
			// connect jSQLite DB file
			String dbFile = "todo.db";
			con = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
			
			// search date
			System.out.println("\n*** Searching data ***");
			Statement stat1 = con.createStatement();
			String sql1 = "select * from TodoList";
			ResultSet rs1 = stat1.executeQuery(sql1);
			while (rs1.next()) {
				String id = rs1.getString("id");
				String cate_id = rs1.getString("cate_id");
				String subject = rs1.getString("subject");
				String content = rs1.getString("content");
				String detail_content = rs1.getString("detail_content");
				String due_date = rs1.getString("due_date");
				String create_date = rs1.getString("create_date");
				String complate_status = rs1.getString("complate_status");
				String delete_status = rs1.getString("delete_status");
				System.out.println(id + " " + subject + " " + due_date);
			}
			stat1.close();
			
//			System.out.println("\n*** 새 데이터 추가 ***");
//			Statement stat2 = con.createStatement();
//			String sql2 = "insert into TodoList (cate_id, subject, content, detail_content, due_date, create_date, complate_status, delete_status)"
//			+ "values ('1', '실프', '개인프로젝트', 'SQLite 기반 TodoList', '10/17 23:59', datetime('now', 'localtime'), 0, 0)";
//			
//			int cnt = stat2.executeUpdate(sql2);
//			if (cnt > 0) {
//				System.out.println("새로운 데이터가 추가되었습니다!");
//			} else {
//				System.out.println("[Error] 데이터 추가 오류!");
//			}
//			stat2.close();
			
//			System.out.println("\n*** 데이터 수정 ***");
//			Statement stat3 = con.createStatement();
//			String sql3 = "update TodoList set due_date = '10/20 23/29'"
//					+ "where id = 1";
////			
//			int cnt3 = stat3.executeUpdate(sql3);
//			if (cnt3 > 0) {
//				System.out.println("데이터가 수정되었습니다!");
//			} else {
//				System.out.println("[Error] 데이터 수정 오류!");
//			}
//			stat3.close();
//			
			System.out.println("\n*** 데이터 삭제 ***");
			Statement stat4 = con.createStatement();
			String sql4 = "delete from TodoList where id = 2";
			
			int cnt4 = stat4.executeUpdate(sql4);
			if (cnt4 > 0) {
				System.out.println("데이터 삭제되었습니다!");
			} else {
				System.out.println("[Error] 데이터 삭제 오류!");
			}
			stat4.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}