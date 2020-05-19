package com.diego.hernando.orchestTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrchestTestApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void test_file_1 () throws  Exception{
		test_file("fichero_1.json",1);
	}

	@Test
	public void test_file_2 () throws  Exception{
		test_file("fichero_2.json",2);
	}

	private void test_file (String path, Integer month) throws  Exception{
		String json = readFile(path);

		mockMvc.perform(MockMvcRequestBuilders.post("/worksigns/").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk());

		String jsonResponse = mockMvc.perform(MockMvcRequestBuilders.get("/weekreports/1/222222222/"+month+"/2018"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		System.out.println(jsonResponse);
	}


	private String readFile (String path){
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
		return getStringFromInputStream(in);
	}

	private String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
}
