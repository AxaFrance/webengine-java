package fr.axa.automation.webengine.boot;

public interface IBootProject {

    public void runFromFramework(String... args) throws Exception ;

    public void runFromProject(String... args) throws Exception ;
}
