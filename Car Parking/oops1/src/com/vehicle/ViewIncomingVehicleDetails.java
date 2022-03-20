package com.vehicle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.connection.DatabaseConnection;

/**
 * Servlet implementation class ViewIncomingVehicleDetails
 */
@SuppressWarnings("serial")
@WebServlet("/ViewIncomingVehicleDetails")
public class ViewIncomingVehicleDetails extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String intime = request.getParameter("intime");
		String rating = request.getParameter("rating");
		String status = request.getParameter("status");
		String service = request.getParameter("service");
		int parkingcharge = 0;
		Date d1 = null;
		Date d2 = null;
		long differenceHours = 0;
		long differenceMinutes = 0;
		HttpSession session = request.getSession();
		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			d1 = format.parse(intime);
			d2 = format.parse(format.format(new Date()));
			long timeDifference = d2.getTime() - d1.getTime();
			differenceHours = timeDifference / (60 * 60 * 1000) % 24;
			differenceMinutes = timeDifference / (60 * 1000) % 60;
			long diffDays = timeDifference / (24 * 60 * 60 * 1000);
			differenceHours = differenceHours + (diffDays * 24);

			System.out.println("Hours  " + differenceHours);

			if (differenceMinutes < 59) {
				parkingcharge = (int)(differenceMinutes*1)+100;
			} else if (differenceHours > 1 ) {
				parkingcharge = (int)(differenceHours*25)+100;
			}  else {
				System.out.println("Bye");
			}

			Connection connection = DatabaseConnection.getConnection();
			Statement statement = connection.createStatement();
			int updateinfo = statement.executeUpdate("update  tblvehicle set Rating='" + rating + "',Status='" + status
					+"',Service='" + service +"',ParkingCharge='" + parkingcharge + "' where ID='" + session.getAttribute("cid") +"'");
			if (updateinfo > 0) {
				response.sendRedirect("manage-incomingvehicle.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
