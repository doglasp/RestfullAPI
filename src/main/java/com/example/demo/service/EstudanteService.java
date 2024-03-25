package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Estudante;
import com.example.demo.entity.Livro;
import com.example.demo.repository.EstudanteRepository;
import com.example.demo.repository.LivroRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EstudanteService {

	private EstudanteRepository estudanteRepository;
	private LivroRepository livroRepository;

	public ResponseEntity<Estudante> buscarEstudantePorId(Long id) {
		if (estudanteRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.OK).body(estudanteRepository.findById(id).get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	public Page<Estudante> buscarTodosEstudantes(PageRequest page) {
		return estudanteRepository.findAll(page);
	}

	public ResponseEntity<Estudante> cadastrarEstudante(Estudante estudante) {
		Set<Livro> livros = estudante.getLivros();
		estudante.setLivros(new HashSet<>());
		Estudante estudantesSalvo = estudanteRepository.save(estudante);
		for(Livro livro: livros) {
			livro.setEstudante(Estudante.builder().id(estudante.getId()).build());
			estudante.getLivros().add(livroRepository.save(livro));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(estudantesSalvo);
	}

	public ResponseEntity<Estudante> atualizarEstudante(Long id, Estudante estudante) {
		if (estudanteRepository.existsById(id)) {
			estudante.setId(id);
			for (Livro livro : estudante.getLivros()) {
				livro.setEstudante(estudante);
			}
			Estudante estudantesSalvo = estudanteRepository.save(estudante);
			return ResponseEntity.status(HttpStatus.OK).body(estudantesSalvo);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	public ResponseEntity<String> removerEstudante(Long id) {
		if (estudanteRepository.existsById(id)) {
			estudanteRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Estudante deletado com sucesso!");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudante não encontrado.");
	}
}
