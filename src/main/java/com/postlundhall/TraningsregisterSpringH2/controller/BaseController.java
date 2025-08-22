package com.postlundhall.TraningsregisterSpringH2.controller;

import com.postlundhall.TraningsregisterSpringH2.service.BaseService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

public abstract class BaseController<T, ID> {

    protected final BaseService<T, ID> service;
    protected final String entityName;
    protected final String viewFolder;
    private final Class<T> entityClass;   // alltid initierad i konstruktorn

    protected BaseController(BaseService<T, ID> service,
                             String entityName,
                             String viewFolder,
                             Class<T> entityClass) {
        this.service = service;
        this.entityName = entityName;
        this.viewFolder = viewFolder;
        this.entityClass = entityClass;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute(entityName + "List", service.findAll());
        return viewFolder + "/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        try {
            model.addAttribute(entityName, entityClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Kunde inte skapa ny instans av " + entityClass, e);
        }
        return viewFolder + "/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable ID id, Model model) {
        model.addAttribute(entityName, service.findById(id));
        return viewFolder + "/form";
    }

    @PostMapping
    public String save(@ModelAttribute T entity) {
        service.save(entity);
        return "redirect:/" + viewFolder;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable ID id) {
        service.deleteById(id);
        return "redirect:/" + viewFolder;
    }
}