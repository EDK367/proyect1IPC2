package com.example.IPC2Backend.controllers;

import com.example.IPC2Backend.models.administrador;
import com.example.IPC2Backend.respository.administradorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequestMapping("/api/v1/")
public class adminControlador {


    @Autowired
    private administradorRepositorio repositorio;

    @GetMapping("admin")
    public List<administrador> listAdmin(){
        return repositorio.findAll();
    }
}
