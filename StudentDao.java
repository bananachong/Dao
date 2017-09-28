package com.lanou.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lanou.entity.Student;
import com.lanou.util.DButil;

public class StudentDao {
	public final int COUNT=6;
	//获取共有多少条信息
	public int  pageCount() throws SQLException {
		Connection connection=DButil.getConnection();
		String sql="select count(*) from school_student where uid=0";
		PreparedStatement ps=connection.prepareStatement(sql);
		ResultSet rs= ps.executeQuery();
		//只需要一个数值，就不需要while循环了
		rs.next();
		//除了getInt（“字段名的形式”）PS（虽然使用字段名确实能有含义的获取）
		//还可以使用getInt（下标的形式）， 这种方式就是写的内容少，但含义不清晰
		int count =rs.getInt(1);
		return count;
		
	}
	
	//分页和模糊查询
	public List<Student> selectForLimit(int page,String likeName) throws SQLException {
		List<Student> students=new ArrayList<Student>();
		Connection con=DButil.getConnection();
		//String sql="select *from school_student where uid=0 limit (页码数-1)*展示的数量，一页展示多少个";
		String str="";
		if(likeName!=null) {
			str="and sname like '%"+likeName+"%'";
		}
		String sql="select *from school_student where uid=0 "+str+" limit ?,?";
				//"select *from school_student where uid=0 limit (page-1)*COUNT,COUNT";设置常量
		PreparedStatement ps=con.prepareStatement(sql);
		ps.setInt(1, (page-1)*COUNT);
		ps.setInt(2, COUNT);
		ResultSet rs= ps.executeQuery();
		while (rs.next()) {
			int sid=rs.getInt("sid");
			String name=rs.getString("sname");
			String username=rs.getString("suername");
			String password=rs.getString("spwd");
			int age=rs.getInt("sage");
			Student student=new Student(sid,name,username,password,age);
			students.add(student);
		}
		return students;
		
	}
	
	
	//根据主键查找信息
	public Student selectById(String sid) throws SQLException {
		Connection con=DButil.getConnection();
		String sql="select * from school_student where sid=?";
		PreparedStatement ps=con.prepareStatement(sql);
		ps.setString(1, sid);
		ResultSet rs=ps.executeQuery();
		rs.next();
		int sId=rs.getInt("sid");
		String name=rs.getString("sname");
		String username=rs.getString("suername");
		String password=rs.getString("spwd");
		int age=rs.getInt("sage");
		
		Student student=new Student(sId,name,username,password,age);
		return student;
		
	}
	
	
	
	//根据主键修改信息
	public boolean updateStudent(Student stu) throws SQLException {
		boolean result1 = false;
		Connection con = DButil.getConnection();
		String sql = "update school_student "
				+ "set sname = ?,suername = ?,spwd = ?,sage = ? where sid = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, stu.getName());
		ps.setString(2, stu.getUsername());
		ps.setString(3, stu.getPsaaword());
		ps.setInt(4, stu.getAge());
		ps.setInt(5, stu.getSid());
		int result = ps.executeUpdate();
		if(result == 1){
			result1 = true;
		}
		return result1;
	}
	//根据主键逻辑删除
public boolean deleteById(int sid) throws SQLException {
		
		Connection con = DButil.getConnection();
		String sql = "update school_student "
				+ "set uid = 1 where sid = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, sid);
		int result = ps.executeUpdate();
		if(result == 1){
			return true;
		}
		DButil.close(ps, con);
		return false;
	}
	//模糊查询
	public List<Student> likes(String string) throws SQLException {
		Connection  con=DButil.getConnection();
		String sql="select * from school_student where suername like ?";
		PreparedStatement ps=con.prepareStatement(sql);
		ps.setString(1, "%"+string+"%");
		ResultSet rs= ps.executeQuery();
		List<Student> list=new ArrayList<Student>();
		while (rs.next()) {
			int sid=rs.getInt("sid");
			String name=rs.getString("sname");
			String username=rs.getString("suername");
			String spwd=rs.getString("spwd");
			int age=rs.getInt("sage");
			Student student=new Student(sid,name,username,spwd,age);
			list.add(student);
		}

		DButil.close(ps, con);
		if(list.isEmpty()) {
			return null;
		}
		return list;
		
		
	}
	
	
	
	public boolean Update(Student stu) throws SQLException {
		Connection con=DButil.getConnection();
		String sql="update school_student set uid=1 where sname=?";
		PreparedStatement ps=con.prepareStatement(sql);
		
		ps.setString(1, stu.getName());
		
		int num=ps.executeUpdate();
		DButil.close(ps, con);
		if(num!=0) {
			return true;
		}else {
			return false;
		}
		
	}
	
	
	
	
	//chazhao查找所有
	public List<Student> selectall() throws SQLException {
		Connection con=DButil.getConnection();
		String sql="select * from school_student where uid=0";
		PreparedStatement ps=con.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		List<Student> list=new ArrayList<Student>();
		
		while (rs.next()) {
			int sid=rs.getInt("sid");
			String name=rs.getString("sname");
			String username=rs.getString("suername");
			String pwd=rs.getString("spwd");
			int age=rs.getInt("sage");
			int uid=rs.getInt("uid");
			Student 	student=new Student(sid,name,username,pwd,age,uid);
			list.add(student);
		}
		if(rs!=null) {
			rs.close();
			
		}
		DButil.close(ps, con);
		
		return list;
		
	}
	
	
	
	
	public Student selectByUserNameAndPwd(Student stu) throws SQLException {
		Connection con=DButil.getConnection();
		String sql="select * from school_student where suername=? and spwd=?";
		PreparedStatement ps=con.prepareStatement(sql);
		ps.setString(1, stu.getUsername());
		ps.setString(2, stu.getPsaaword());
		ResultSet rs=ps.executeQuery();
		Student student=null;
		
		while (rs.next()) {
			int sid=rs.getInt("sid");
			String name=rs.getString("sname");
			String username=rs.getString("suername");
			String pwd=rs.getString("spwd");
			int age=rs.getInt("sage");
			student=new Student(sid,name,username,pwd,age);
		}
		if(rs!=null) {
			rs.close();
			
		}
		DButil.close(ps, con);
		
		return student;
		
	}
	
	
	//按用户名查找学生的信息
	public List<Student> selectByUserName(String UserName) throws SQLException {
		Connection connection=DButil.getConnection();
		String sql="select * from school_student where suername=?";
		PreparedStatement ps=connection.prepareStatement(sql);
		ps.setString(1,UserName);
		ResultSet rs=ps.executeQuery();
		List<Student> students=new ArrayList<Student>();
		
		while (rs.next()) {
			int sid=rs.getInt("sid");
			String names=rs.getString("sname");
			String username=rs.getString("suername");
			String pwd=rs.getString("spwd");
			int age =rs.getInt("sage");
			Student stu= new Student(sid,names,username,pwd,age);
			students.add(stu);
		}
		
		return students;
	}
	//增加学生信息的方法
	public boolean addStudent(Student stu) throws SQLException {
		Connection con=DButil.getConnection();
		String sql="insert into school_student(sname,suername,spwd,sage,uid)values(?,?,?,?,0)";
		PreparedStatement ps=con.prepareStatement(sql);
		ps.setString(1, stu.getName());
		ps.setString(2, stu.getUsername());
		ps.setString(3, stu.getPsaaword());
		ps.setInt(4, stu.getAge());
		
		int result=ps.executeUpdate();
		if( result!=1) {
			return false;
		}
		
		
		return true;
	}
	
}
