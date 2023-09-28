package br.com.alurafood.pedidos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.alurafood.pedidos.dto.PedidoDTO;
import br.com.alurafood.pedidos.dto.StatusDTO;
import br.com.alurafood.pedidos.model.Pedido;
import br.com.alurafood.pedidos.model.Status;
import br.com.alurafood.pedidos.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PedidoService {

	private PedidoRepository repository;
	private final ModelMapper modelMapper;
	
	public PedidoService (PedidoRepository repository, ModelMapper modelMapper ) {
		this.repository = repository;
		this.modelMapper = modelMapper;
	}

	public List<PedidoDTO> obterTodos() {
		return repository.findAll().stream()
				.map(p -> modelMapper.map(p, PedidoDTO.class))
				.toList();
	}

	public PedidoDTO obterPorId(Long id) {
		Pedido pedido = repository.findById(id).orElseThrow(EntityNotFoundException::new);

		return modelMapper.map(pedido, PedidoDTO.class);
	}

	public PedidoDTO criarPedido(PedidoDTO DTO) {
		Pedido pedido = modelMapper.map(DTO, Pedido.class);

		pedido.setDataHora(LocalDateTime.now());
		pedido.setStatus(Status.REALIZADO);
		pedido.getItens().forEach(item -> item.setPedido(pedido));
		repository.save(pedido);

		return modelMapper.map(pedido, PedidoDTO.class);
	}

	public PedidoDTO atualizaStatus(Long id, StatusDTO DTO) {

		Pedido pedido = repository.porIdComItens(id);

		if (pedido == null) {
			throw new EntityNotFoundException();
		}

		pedido.setStatus(DTO.getStatus());
		repository.save(pedido);
		return modelMapper.map(pedido, PedidoDTO.class);
	}

	public void aprovaPagamentoPedido(Long id) {

		Pedido pedido = repository.porIdComItens(id);

		if (pedido == null) {
			throw new EntityNotFoundException();
		}

		pedido.setStatus(Status.PAGO);
		repository.save(pedido);
	}
}
