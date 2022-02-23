package com.dalbit.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event/dallagers")
@Scope("prototype")
@Slf4j
public class DallagersEventController {


}
