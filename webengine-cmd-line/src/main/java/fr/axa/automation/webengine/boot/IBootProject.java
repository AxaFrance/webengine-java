package fr.axa.automation.webengine.boot;

public interface IBootProject {

    void runFromFramework(String... args) throws Exception ;

    void runFromProject(String... args) throws Exception ;
}
