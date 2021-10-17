import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

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
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Scanner sc = new Scanner(System.in);
		
		boolean quit = false;
		int id, cate_id;
		String subject, content, detail_content, due_date;
		
		do {
			prompt(false);
			String choice = sc.next();
			
			switch (choice) {

			case "help":
				prompt(true);
				break;
				
			case "ls":
				searchItem(con);
				break;
			
			case "add":
				System.out.print("Category를 선택하시오 (공부 : 1, 과제 : 2) > ");
				cate_id = sc.nextInt();
				System.out.print("과목을 입력하시오 > ");
				subject = sc.next();
				System.out.print("내용을 입력하시오 > ");
				content = sc.next();
				System.out.print("세부 사항을 입력하시오 > ");
				detail_content = sc.next();
				System.out.print("마감 기한을 입력하시오 > ");
				due_date = sc.next();
				
				addItem(con, cate_id, subject, content, detail_content, due_date);
				break;
				
			case "com":
				System.out.print("완료 여부를 체크할 id를 선택하시오.");
				id = sc.nextInt();
				
				checkComplate(con, id);
				break;
				
			case "mul_com":
				checkMultiComplate(con);
				break;
			
			case "del":
				System.out.print("삭제할 id를 선택하시오.");
				id = sc.nextInt();
				
				deleteItem(con, id);
				break;
				
			case "mul_del":
				multiDelItem(con);
				break;
	
			case "edit":
				System.out.print("수정할 id를 선택하시오.");
				id = sc.nextInt();
				System.out.print("수정된 내용을 입력하시오 > ");
				content = sc.next();
				System.out.print("수정된 세부 사항을 입력하시오 > ");
				detail_content = sc.next();
				System.out.print("수정된 마감 기한을 입력하시오 > ");
				due_date = sc.next();
				
				reviseItem(con, id, content, detail_content, due_date);
				break;

			case "exit":
				quit = true;
				break;

			default:
				System.out.println("위의 명령어 중 하나를 선택해주세요.");
				break;
			}
			
		} while (!quit);
	}
	
	private static void displaymenu()
    {
        System.out.println();
        System.out.println("1. 아이템 리스트 보기 ( ls )");
        System.out.println("2. 아이템 추가 ( add )");
        System.out.println("3. 완료 여부 체크 ( com )");
        System.out.println("4. 완료 여부 동시 체크 ( mul_com )");
        System.out.println("5. 아이템 삭제 ( del )");
        System.out.println("6. 아이템 동시 삭제 ( mul_del )");
        System.out.println("7. 아이템 업데이트  ( edit )");
        System.out.println("8. 종료 (Or press escape key to exit)");
        System.out.println();
    }
    
    public static void prompt(boolean tf) {
    	if (tf == true) {
    		displaymenu();
    	} else {
    		System.out.print("명령어를 선택하시오 > ");
    	}
    }
    
    public static void searchItem(Connection con) {
		try {
			System.out.println("\n*** Searching data ***");
			Statement stat1 = con.createStatement();
			String sql1 = "select * from TodoList inner join Category on TodoList.cate_id = Category.id";
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
				String name = rs1.getString("name");
				System.out.println(id + ". " + "[" + name + "] " + subject + ", 내용: " + content + ", 세부사항: "
				+ detail_content+ ", 마감일: " + due_date + ", 완료여부: " + complate_status);
			}
			stat1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addItem(Connection con, int cate_id, String subject, String content, String detail_content, String due_date) {
		try {
			System.out.println("\n*** 새 데이터 추가 ***");
			Statement stat2 = con.createStatement();
			
			String sql2 = "insert into TodoList (cate_id, subject, content, detail_content, due_date, create_date, complate_status)"
			+ "values (" + cate_id + ", '" + subject + "', '" + content + "', '" + detail_content + "', '" + due_date + "', datetime('now', 'localtime'), 0)";
			
			int cnt = stat2.executeUpdate(sql2);
			if (cnt > 0) {
				System.out.println("새로운 데이터가 추가되었습니다!");
			} else {
				System.out.println("[Error] 데이터 추가 오류!");
			}
			stat2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void reviseItem(Connection con, int id, String content, String detail_content, String due_date) {
		try {
			System.out.println("\n*** 데이터 수정 ***");
			Statement stat1 = con.createStatement();
			String sql1 = "update TodoList set content = '" + content + "' "
					+ "where id = " + id;
			int cnt1 = stat1.executeUpdate(sql1);
			stat1.close();
			
			Statement stat2 = con.createStatement();
			String sql2 = "update TodoList set detail_content = '" + detail_content + "' "
					+ "where id = " + id;
			int cnt2 = stat2.executeUpdate(sql2);
			stat2.close();
			
			Statement stat3 = con.createStatement();
			String sql3 = "update TodoList set due_date = '" + due_date+ "' "
					+ "where id = " + id;
			int cnt3 = stat3.executeUpdate(sql3);
			
			if (cnt1 > 0 && cnt2 > 0 && cnt3 > 0) {
				System.out.println("데이터가 수정되었습니다!");
			} else {
				System.out.println("[Error] 데이터 수정 오류!");
			}
			stat3.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void checkComplate(Connection con, int id) {
		try {
			System.out.println("\n*** 완료 여부 체크 ***");
			Statement stat1 = con.createStatement();
			String sql1 = "update TodoList set complate_status = '" + 1 + "' "
					+ "where id = " + id;
			int cnt1 = stat1.executeUpdate(sql1);
			stat1.close();
			
			if (cnt1 > 0) {
				System.out.println("완료 여부가 체크되었습니다!");
			} else {
				System.out.println("[Error] 완료 여부 체크 오류!");
			}
			stat1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void checkMultiComplate(Connection con) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("\n*** 완료 여부 체크 ***");
		
		while (true) {
			System.out.println("완료 여부를 체크할 id (그만하려면 0 입력.) > ");
			int id = sc.nextInt();
			
			if (id == 0) {
				break;
			}
			
			try {
				
				Statement stat1 = con.createStatement();
				String sql1 = "update TodoList set complate_status = '" + 1 + "' "
						+ "where id = " + id;
				int cnt1 = stat1.executeUpdate(sql1);
				stat1.close();
				
				if (cnt1 > 0) {
					System.out.println(id + "의 완료 여부가 체크되었습니다!");
				} else {
					System.out.println("[Error] 완료 여부 체크 오류!");
				}
				stat1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
		
	public static void deleteItem(Connection con, int id) {
		try {
			System.out.println("\n*** 데이터 삭제 ***");
			Statement stat4 = con.createStatement();
			String sql4 = "delete from TodoList where id = " + id;
			
			int cnt4 = stat4.executeUpdate(sql4);
			if (cnt4 > 0) {
				System.out.println("데이터 삭제되었습니다!");
			} else {
				System.out.println("[Error] 데이터 삭제 오류!");
			}
			stat4.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void multiDelItem(Connection con) {
		Scanner sc = new Scanner(System.in);
				
		System.out.println("\n*** 데이터 삭제 ***");
		
		while (true) {
			System.out.println("삭제할 id (그만하려면 0 입력.) > ");
			int id = sc.nextInt();
			
			if (id == 0) {
				break;
			}
			
			try {
				
				Statement stat4 = con.createStatement();
				String sql4 = "delete from TodoList where id = " + id;
				
				int cnt4 = stat4.executeUpdate(sql4);
				if (cnt4 > 0) {
					System.out.println("데이터 삭제되었습니다!");
				} else {
					System.out.println("[Error] 데이터 삭제 오류!");
				}
				stat4.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}