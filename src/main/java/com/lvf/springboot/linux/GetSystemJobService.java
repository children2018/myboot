package com.lvf.springboot.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class GetSystemJobService {

	public List<String> executeNewFlow(List<String> commands) {
		List<String> rspList = new ArrayList<String>();
		Runtime run = Runtime.getRuntime();
		try {
			Process proc = run.exec("/bin/bash", null, null);
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(proc.getOutputStream(), Charset.forName("utf-8")));
			for (String line : commands) {
				out.println(line);
			}
			// out.println("cd /home/test");
			// out.println("pwd");
			// out.println("rm -fr /home/proxy.log");
			out.println("exit");// 这个命令必须执行，否则in流不结束。
			String rspLine = "";
			while ((rspLine = in.readLine()) != null) {
				System.out.println(rspLine);
				rspList.add(rspLine);
			}
			proc.waitFor();
			in.close();
			out.close();
			proc.destroy();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rspList;
	}

	public static void main(String[] args) {
		List<String> commands = new ArrayList<>();
		commands.add("cd /home/yaoms");
		commands.add("touch test.txt");
		new GetSystemJobService().executeNewFlow(commands);
	}

}
