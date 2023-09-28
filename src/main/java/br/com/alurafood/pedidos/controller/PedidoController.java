package br.com.alurafood.pedidos.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alurafood.pedidos.dto.PedidoDTO;
import br.com.alurafood.pedidos.dto.StatusDTO;
import br.com.alurafood.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	private PedidoService service;

	public PedidoController(PedidoService service) {
		this.service = service;
	}

	@GetMapping
	public List<PedidoDTO> listarTodos() {
		return service.obterTodos();
	}

	@GetMapping("/{id}")
	public ResponseEntity<PedidoDTO> listarPorId(@PathVariable @NotNull Long id) {
		PedidoDTO pedidoDTO = service.obterPorId(id);

		return ResponseEntity.ok(pedidoDTO);
	}

	@PostMapping
	public ResponseEntity<PedidoDTO> realizaPedido(@RequestBody @Valid PedidoDTO pedidoDTO,
			UriComponentsBuilder uriBuilder) {
		PedidoDTO pedidoRealizado = service.criarPedido(pedidoDTO);

		URI endereco = uriBuilder.path("/pedidos/{id}").buildAndExpand(pedidoRealizado.getId()).toUri();

		return ResponseEntity.created(endereco).body(pedidoRealizado);

	}

	@PutMapping("/{id}/status")
	public ResponseEntity<PedidoDTO> atualizaStatus(@PathVariable Long id, @RequestBody StatusDTO status) {
		PedidoDTO pedidoDTO = service.atualizaStatus(id, status);

		return ResponseEntity.ok(pedidoDTO);
	}

	@PutMapping("/{id}/pago")
	public ResponseEntity<Void> aprovaPagamento(@PathVariable @NotNull Long id) {
		service.aprovaPagamentoPedido(id);

		return ResponseEntity.ok().build();

	}

}
