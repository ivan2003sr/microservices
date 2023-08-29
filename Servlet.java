package com.ivan.rest.webservices.restfulwebservices;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.util.StringUtils;

import javax.xml.crypto.dsig.XMLObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

interface TemplateManager {
    public void addTemplate(File document) throws Exception;

    public void clearTemplates();

    public void refreshTemplate(String templateName) throws Exception;
}

public class Servlet extends HttpServlet {

    private static String result;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String templateType = req.getParameter("templateType");
        String templateFile = req.getParameter("templateFile");

        refreshTemplates(templateType, templateFile);
    }

    private void refreshTemplates(String templateType, String templateFile) {
        try {
            RefreshTemplates rt = new RefreshTemplates();
            setResult(null);
            rt.doRefresh(templateType, templateFile);
            setResult(rt.getResult());
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void setResult(String result) {
        Servlet.result = result;
    }


}

class RefreshTemplates {


    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void doRefresh(String templateType, String templateFile) {
        setResult(null);
        setResult("Template ::" + templateFile + " was refreshed");

        try {
            ((TemplateManager) ApplicationContextLocator.getBean(templateType)).refreshTemplate(templateFile);
        } catch (Exception e) {

        }
    }

}

class ApplicationContextLocator implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getContext() {
        return applicationContext;
    }

    public static Object getBean(final String beanName) {
        return applicationContext.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextLocator.applicationContext = applicationContext;
    }
}

abstract class AbstractTemplateManager implements TemplateManager {

    private final Map<String, TransformationDocument> transformationDocuments = new HashMap<String, TransformationDocument>();

    public void refreshTemplate(String templateName) throws Exception {
        if (!templateName.contains("..")) {
            TransformationDocument transformDocument = this.transformationDocuments.get(templateName);

            File file = transformDocument.getSource();

            if (file != null && file.exists()) {

                this.addTemplate(file);
            }

        }else {
            throw new IllegalArgumentException();
        }

    }


    private boolean lazyLoad = false;
    private final Map<String, File> filesAvailable = new HashMap<String, File>();

    public void addTemplate(File file) throws Exception{
        String transformationDocumentKey = fileToTransformationDocumentKey(file);
        if(lazyLoad) {
            synchronized (filesAvailable) {
                filesAvailable.put(transformationDocumentKey, file);
            }
        } else {
            synchronized (transformationDocuments){
                file.getName();
                TransformationDocument transformationDocument = compileTemplate(file);
                transformationDocuments.put(transformationDocumentKey, transformationDocument );
            }
        }
    }

    protected String fileToTransformationDocumentKey (File document){
        return document.getName();
    }
    protected abstract TransformationDocument compileTemplate(File file) throws Exception;

    public Object getTemplate(String templateName) throws ChangeSetPersister.NotFoundException{
        synchronized (transformationDocuments){
            if(transformationDocuments.containsKey(templateName)){
                return (transformationDocuments.get(templateName)).getCompiledTemplate();
            }
        }
        synchronized (filesAvailable){
            if(filesAvailable.containsKey(templateName)){
                try {
                    File file = filesAvailable.get(templateName);
                    String transformationDocumentKey = fileToTransformationDocumentKey(file);
                    TransformationDocument transformationDocument;

                    synchronized (transformationDocuments){
                        file.getName();
                        transformationDocument = compileTemplate(file);
                        transformationDocuments.put(transformationDocumentKey, transformationDocument);
                    }

                    return transformationDocument.getCompiledTemplate();

                } catch (Exception e){
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        throw new ChangeSetPersister.NotFoundException();
    }


}

class TransformationDocument {
    private File source;
    private Object compiledTemplate = null;

    public TransformationDocument(File source, Object compiledTemplate) {
        this.source = source;
        this.compiledTemplate = compiledTemplate;
    }

    public File getSource() {
        return source;
    }

    public Object getCompiledTemplate() {
        return compiledTemplate;
    }


}

class DfgtemplateManagerImpl extends AbstractTemplateManager implements DfgTemplateManager{

    public DfgTemplateManager getDfgTemplate(String dfgTemplateName) throws ChangeSetPersister.NotFoundException{
        return (DfgTemplateManager) super.getTemplate(dfgTemplateName);
    }
@Override
    protected String fileToTransformationDocumentKey(File document){
        return StringUtils.stripFilenameExtension(document.getName());
    }
@Override
    protected TransformationDocument compileTemplate(File file) throws Exception{
        FileInputStream fis = new FileInputStream(file);
        try{
            DfgDocumentTemplate dfgDocumentTemplate = new DfgDocumentTemplate();
            return  new TransformationDocument(file, dfgDocumentTemplate);

        }finally {
            fis.close();
        }
    }


    @Override
    public void clearTemplates() {

    }
}

class DfgDocumentTemplate{

}

interface DfgTemplateManager{

    public DfgTemplateManager getDfgTemplate(String dfgTemplate) throws ChangeSetPersister.NotFoundException;

}

