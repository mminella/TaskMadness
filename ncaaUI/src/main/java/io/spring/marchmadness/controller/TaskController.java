/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.spring.marchmadness.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.task.repository.TaskExplorer;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Michael Minella
 */
@Controller
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private TaskExplorer taskExplorer;

	@RequestMapping(value = {""}, method = RequestMethod.GET)
	public String list(Pageable pageable, Model model) {
		model.addAttribute("page", taskExplorer.findAll(pageable));

		return "task/list";
	}

	@RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
	public String detail(@PathVariable("id") long id, Model model) {
		model.addAttribute("task", taskExplorer.getTaskExecution(id));

		return "task/detail";
	}
}