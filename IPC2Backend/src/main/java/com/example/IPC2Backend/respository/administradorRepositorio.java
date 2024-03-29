package com.example.IPC2Backend.respository;

import com.example.IPC2Backend.models.administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface administradorRepositorio extends JpaRepository <administrador, Long>{

}
