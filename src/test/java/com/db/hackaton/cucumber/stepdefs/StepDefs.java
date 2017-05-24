package com.db.hackaton.cucumber.stepdefs;

import com.db.hackaton.HackatonApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = HackatonApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
