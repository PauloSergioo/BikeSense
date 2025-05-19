package com.bikesense;

import com.bikesense.model.*;
import com.bikesense.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class BikesenseApplication implements CommandLineRunner {

	private final ClimaService climaService;
	private final RotaService rotaService;
	private final GrupoService grupoService;
	private final PostagemService postagemService;
	private final ParticipanteGrupoService participanteService;
	private final RotaRealizadaService rotaRealizadaService;
	private final UsuarioService usuarioService;
	private final Scanner scanner;
	private boolean sistemaAtivo = true;

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	public BikesenseApplication(ClimaService climaService, RotaService rotaService,
								GrupoService grupoService, PostagemService postagemService,
								ParticipanteGrupoService participanteService,
								RotaRealizadaService rotaRealizadaService,
								UsuarioService usuarioService) {
		this.climaService = climaService;
		this.rotaService = rotaService;
		this.grupoService = grupoService;
		this.postagemService = postagemService;
		this.participanteService = participanteService;
		this.rotaRealizadaService = rotaRealizadaService;
		this.usuarioService = usuarioService;
		this.scanner = new Scanner(System.in);
	}

	public static void main(String[] args) {
		SpringApplication.run(BikesenseApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("🚴 BikeSense - Sistema de Rotas Ciclísticas 🚴");
		System.out.println("♻️  Apoiando a mobilidade sustentável e o meio ambiente ♻️");

		while (sistemaAtivo) {
			exibirMenuPrincipal();
			String opcao = scanner.nextLine();

			switch (opcao) {
				case "1":
					consultarClimaAtual();
					break;
				case "2":
					gerenciarRotas();
					break;
				case "3":
					gerenciarGrupos();
					break;
				case "4":
					gerenciarPostagens();
					break;
				case "5":
					gerenciarParticipantes();
					break;
				case "6":
					registrarRotaRealizada();
					break;
				case "7":
					exibirEstatisticasClimaticas();
					break;
				case "0":
					encerrarSistema();
					break;
				default:
					System.out.println("❌ Opção inválida!");
			}
		}
	}

	private void exibirMenuPrincipal() {
		System.out.println("\n=== MENU PRINCIPAL ===");
		System.out.println("1 - Consultar clima atual");
		System.out.println("2 - Gerenciar rotas");
		System.out.println("3 - Gerenciar grupos de pedal");
		System.out.println("4 - Gerenciar postagens");
		System.out.println("5 - Gerenciar participantes");
		System.out.println("6 - Registrar rota realizada");
		System.out.println("7 - Estatísticas climáticas");
		System.out.println("0 - Sair");
		System.out.print("Escolha uma opção: ");
	}

	private void consultarClimaAtual() {
		System.out.println("\n🔍 Consultando clima atual...");
		Clima clima = climaService.getClimaAtual();

		System.out.println("\n=== CLIMA ATUAL ===");
		System.out.println("📍 Local: " + clima.getCidade() + "/" + clima.getEstado());
		System.out.println("🌡️ Temperatura: " + clima.getTemperatura());
		System.out.println("🌤️ Previsão: " + clima.getPrevisao());
		System.out.println("🍃 Qualidade do Ar: " + clima.getQualidadeDoAr());
		System.out.println("☀️ Índice UV: " + clima.getIndiceUv());
		System.out.println("🕒 Registrado em: " + clima.getDataHoraRegistro().format(dateTimeFormatter));

		aguardarEnter();
	}

	private void gerenciarRotas() {
		boolean voltar = false;

		while (!voltar) {
			System.out.println("\n=== GERENCIAMENTO DE ROTAS ===");
			System.out.println("1 - Listar todas as rotas");
			System.out.println("2 - Buscar rota por ID");
			System.out.println("3 - Cadastrar nova rota");
			System.out.println("4 - Atualizar rota");
			System.out.println("5 - Excluir rota");
			System.out.println("0 - Voltar ao menu principal");
			System.out.print("Escolha uma opção: ");

			String opcao = scanner.nextLine();

			switch (opcao) {
				case "1":
					listarTodasRotas();
					break;
				case "2":
					buscarRotaPorId();
					break;
				case "3":
					cadastrarNovaRota();
					break;
				case "4":
					atualizarRota();
					break;
				case "5":
					excluirRota();
					break;
				case "0":
					voltar = true;
					break;
				default:
					System.out.println("❌ Opção inválida!");
			}
		}
	}

	private void listarTodasRotas() {
		System.out.println("\n🔍 Buscando rotas cadastradas...");
		List<Rota> rotas = rotaService.listarTodas();

		if (rotas.isEmpty()) {
			System.out.println("\n⚠️ Nenhuma rota cadastrada!");
		} else {
			System.out.println("\n=== ROTAS CADASTRADAS ===");
			rotas.forEach(this::exibirDetalhesRota);
		}
		aguardarEnter();
	}

	private void buscarRotaPorId() {
		Long id = lerLong("\n🔍 Digite o ID da rota (ou 0 para cancelar): ");
		if (id == 0) {
			System.out.println("Operação cancelada.");
			return;
		}

		Optional<Rota> rota = rotaService.buscarPorId(id);
		if (rota.isPresent()) {
			System.out.println("\n=== DETALHES DA ROTA ===");
			exibirDetalhesRota(rota.get());
		} else {
			System.out.println("\n⚠️ Nenhuma rota encontrada com o ID " + id);
		}
		aguardarEnter();
	}

	private void cadastrarNovaRota() {
		System.out.println("\n📝 Cadastrando nova rota...");
		Rota rota = new Rota();

		rota.setNome(lerStringObrigatoria("Nome da rota: "));
		rota.setDistanciaKm(lerDouble("Distância em km: "));
		rota.setNivel(lerStringObrigatoria("Nível de dificuldade: "));
		rota.setTipo(lerStringObrigatoria("Tipo de trajeto: "));
		rota.setQualidadeAr(lerString("Qualidade do ar (opcional): "));
		rota.setPrevisaoTempo(lerString("Previsão do tempo (opcional): "));
		rota.setClima(lerString("Clima (opcional): "));
		rota.setDescricao(lerString("Descrição (opcional): "));

		Rota rotaSalva = rotaService.criarRota(rota);
		System.out.println("\n✅ Rota cadastrada com sucesso! ID: " + rotaSalva.getId());
		aguardarEnter();
	}

	private void exibirEstatisticasClimaticas() {
		System.out.println("\n📊 Estatísticas Climáticas");
		System.out.println("1 - Qualidade do ar por local");
		System.out.println("2 - Frequência de condições climáticas");
		System.out.println("0 - Voltar");
		System.out.print("Escolha uma opção: ");

		String opcao = scanner.nextLine();

		switch (opcao) {
			case "1":
				System.out.println("\n📈 Qualidade do Ar por Local");
				System.out.println("Em desenvolvimento...");
				break;
			case "2":
				System.out.println("\n📈 Frequência de Condições Climáticas");
				System.out.println("Em desenvolvimento...");
				break;
			case "0":
				return;
			default:
				System.out.println("❌ Opção inválida!");
		}
		aguardarEnter();
	}

	private String lerString(String mensagem) {
		System.out.print(mensagem);
		return scanner.nextLine().trim();
	}

	private String lerStringObrigatoria(String mensagem) {
		while (true) {
			String valor = lerString(mensagem);
			if (!valor.isEmpty()) {
				return valor;
			}
			System.out.println("❌ Este campo é obrigatório!");
		}
	}

	private Long lerLong(String mensagem) {
		while (true) {
			try {
				System.out.print(mensagem);
				String input = scanner.nextLine();
				if (input.equalsIgnoreCase("0")) {
					return 0L;
				}
				return Long.parseLong(input);
			} catch (NumberFormatException e) {
				System.out.println("❌ Valor inválido! Digite um número.");
			}
		}
	}

	private Double lerDouble(String mensagem) {
		while (true) {
			try {
				System.out.print(mensagem);
				return Double.parseDouble(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("❌ Valor inválido! Digite um número.");
			}
		}
	}

	private LocalDate lerData(String mensagem) {
		while (true) {
			try {
				System.out.print(mensagem + " (DD/MM/AAAA): ");
				String input = scanner.nextLine();
				return LocalDate.parse(input, dateFormatter);
			} catch (Exception e) {
				System.out.println("❌ Data inválida! Use o formato DD/MM/AAAA.");
			}
		}
	}

	private void aguardarEnter() {
		System.out.print("\n⏎ Pressione Enter para continuar...");
		scanner.nextLine();
	}

	private void encerrarSistema() {
		System.out.println("\n⏳ Encerrando o sistema...");
		this.sistemaAtivo = false;
		scanner.close();
		System.out.println("✅ Sistema encerrado com sucesso!");
	}

	private void gerenciarGrupos() {
		boolean voltar = false;

		while (!voltar) {
			System.out.println("\n=== GERENCIAMENTO DE GRUPOS ===");
			System.out.println("1 - Listar todos os grupos");
			System.out.println("2 - Buscar grupo por ID");
			System.out.println("3 - Cadastrar novo grupo");
			System.out.println("4 - Adicionar participante ao grupo");
			System.out.println("5 - Listar participantes de um grupo");
			System.out.println("0 - Voltar ao menu principal");
			System.out.print("Escolha uma opção: ");

			String opcao = scanner.nextLine();

			switch (opcao) {
				case "1":
					listarTodosGrupos();
					break;
				case "2":
					buscarGrupoPorId();
					break;
				case "3":
					cadastrarNovoGrupo();
					break;
				case "4":
					adicionarParticipanteAoGrupo();
					break;
				case "5":
					listarParticipantesDoGrupo();
					break;
				case "0":
					voltar = true;
					break;
				default:
					System.out.println("❌ Opção inválida!");
			}
		}
	}

	private void listarTodosGrupos() {
		System.out.println("\n🔍 Listando todos os grupos...");
		List<Grupo> grupos = grupoService.listarTodosGrupos();

		if (grupos.isEmpty()) {
			System.out.println("\n⚠️ Nenhum grupo cadastrado!");
		} else {
			System.out.println("\n=== GRUPOS CADASTRADOS ===");
			grupos.forEach(this::exibirDetalhesGrupo);
		}
		aguardarEnter();
	}

	private void buscarGrupoPorId() {
		Long id = lerLong("\n🔍 Digite o ID do grupo (ou 0 para cancelar): ");
		if (id == 0) return;

		Optional<Grupo> grupo = grupoService.buscarPorId(id);
		if (grupo.isPresent()) {
			System.out.println("\n=== DETALHES DO GRUPO ===");
			exibirDetalhesGrupo(grupo.get());
		} else {
			System.out.println("\n⚠️ Grupo não encontrado!");
		}
		aguardarEnter();
	}

	private void cadastrarNovoGrupo() {
		System.out.println("\n📝 Cadastrando novo grupo...");
		Grupo grupo = new Grupo();

		grupo.setNome(lerStringObrigatoria("Nome do grupo: "));
		grupo.setOrigem(lerString("Origem (opcional): "));
		grupo.setDestino(lerString("Destino (opcional): "));
		grupo.setDia(lerData("Data do pedal"));
		grupo.setHorario(lerHorario("Horário do pedal"));
		grupo.setNivel(lerStringObrigatoria("Nível de dificuldade: "));
		grupo.setTipoTrajeto(lerString("Tipo de trajeto (opcional): "));
		grupo.setComentario(lerString("Comentário (opcional): "));

		Grupo grupoSalvo = grupoService.criarGrupo(grupo);
		System.out.println("\n✅ Grupo criado com sucesso! ID: " + grupoSalvo.getId());
		aguardarEnter();
	}

	private void adicionarParticipanteAoGrupo() {
		Long grupoId = lerLong("\n🔍 Digite o ID do grupo: ");
		if (grupoId == 0) return;

		System.out.println("\n👤 Cadastrando novo participante...");
		ParticipanteGrupo participante = new ParticipanteGrupo();

		participante.setNome(lerStringObrigatoria("Nome do participante: "));
		participante.setTelefone(lerString("Telefone (opcional): "));

		Grupo grupo = new Grupo();
		grupo.setId(grupoId);
		participante.setGrupo(grupo);

		try {
			ParticipanteGrupo participanteSalvo = participanteService.criarParticipante(participante);
			System.out.println("\n✅ Participante adicionado com sucesso! ID: " + participanteSalvo.getId());
		} catch (Exception e) {
			System.out.println("\n❌ Erro: " + e.getMessage());
		}
		aguardarEnter();
	}

	private void listarParticipantesDoGrupo() {
		Long grupoId = lerLong("\n🔍 Digite o ID do grupo: ");
		if (grupoId == 0) return;

		List<ParticipanteGrupo> participantes = participanteService.buscarPorGrupo(grupoId);

		if (participantes.isEmpty()) {
			System.out.println("\n⚠️ Nenhum participante encontrado para este grupo!");
		} else {
			System.out.println("\n=== PARTICIPANTES DO GRUPO ===");
			participantes.forEach(p -> {
				System.out.println("\n🆔 ID: " + p.getId());
				System.out.println("👤 Nome: " + p.getNome());
				System.out.println("📞 Telefone: " + (p.getTelefone() != null ? p.getTelefone() : "Não informado"));
				System.out.println("----------------------------------");
			});
		}
		aguardarEnter();
	}

	private void exibirDetalhesGrupo(Grupo grupo) {
		System.out.println("\n🆔 ID: " + grupo.getId());
		System.out.println("👥 Nome: " + grupo.getNome());
		System.out.println("📍 Origem: " + (grupo.getOrigem() != null ? grupo.getOrigem() : "Não informada"));
		System.out.println("🏁 Destino: " + (grupo.getDestino() != null ? grupo.getDestino() : "Não informado"));
		System.out.println("📅 Data: " + grupo.getDia().format(dateFormatter));
		System.out.println("⏰ Horário: " + grupo.getHorario().format(timeFormatter));
		System.out.println("⚡ Nível: " + grupo.getNivel());
		System.out.println("🛣️ Tipo de trajeto: " + (grupo.getTipoTrajeto() != null ? grupo.getTipoTrajeto() : "Não informado"));
		System.out.println("💬 Comentário: " + (grupo.getComentario() != null ? grupo.getComentario() : "Nenhum"));
		System.out.println("----------------------------------");
	}

	private void gerenciarPostagens() {
		boolean voltar = false;

		while (!voltar) {
			System.out.println("\n=== GERENCIAMENTO DE POSTAGENS ===");
			System.out.println("1 - Listar todas as postagens");
			System.out.println("2 - Buscar postagem por ID");
			System.out.println("3 - Criar nova postagem");
			System.out.println("0 - Voltar ao menu principal");
			System.out.print("Escolha uma opção: ");

			String opcao = scanner.nextLine();

			switch (opcao) {
				case "1":
					listarTodasPostagens();
					break;
				case "2":
					buscarPostagemPorId();
					break;
				case "3":
					criarNovaPostagem();
					break;
				case "0":
					voltar = true;
					break;
				default:
					System.out.println("❌ Opção inválida!");
			}
		}
	}

	private void listarTodasPostagens() {
		System.out.println("\n🔍 Listando todas as postagens...");
		List<Postagem> postagens = postagemService.listarTodas();

		if (postagens.isEmpty()) {
			System.out.println("\n⚠️ Nenhuma postagem cadastrada!");
		} else {
			System.out.println("\n=== POSTAGENS RECENTES ===");
			postagens.forEach(this::exibirDetalhesPostagem);
		}
		aguardarEnter();
	}

	private void buscarPostagemPorId() {
		Long id = lerLong("\n🔍 Digite o ID da postagem (ou 0 para cancelar): ");
		if (id == 0) return;

		Optional<Postagem> postagem = postagemService.buscarPorId(id);
		if (postagem.isPresent()) {
			System.out.println("\n=== DETALHES DA POSTAGEM ===");
			exibirDetalhesPostagem(postagem.get());
		} else {
			System.out.println("\n⚠️ Postagem não encontrada!");
		}
		aguardarEnter();
	}

	private void criarNovaPostagem() {
		System.out.println("\n📝 Criando nova postagem...");
		Postagem postagem = new Postagem();

		postagem.setNomeUsuario(lerStringObrigatoria("Seu nome: "));
		postagem.setCidade(lerStringObrigatoria("Cidade: "));
		postagem.setEstado(lerStringObrigatoria("Estado (sigla): "));
		postagem.setDescricao(lerStringObrigatoria("Descrição: "));
		postagem.setDificuldadePercorrida(lerString("Dificuldade percorrida (opcional): "));
		postagem.setQualidadeAr(lerString("Qualidade do ar (opcional): "));
		postagem.setTemperaturaEstimada(lerString("Temperatura estimada (opcional): "));
		postagem.setImagemUrl(lerString("URL da imagem (opcional): "));
		postagem.setDataHora(LocalDateTime.now());

		if (lerSimNao("Deseja associar a uma rota? (S/N): ")) {
			Long rotaId = lerLong("ID da rota: ");
			Rota rota = new Rota();
			rota.setId(rotaId);
			postagem.setRota(rota);
		}

		try {
			Postagem postagemSalva = postagemService.criarPostagem(postagem);
			System.out.println("\n✅ Postagem criada com sucesso! ID: " + postagemSalva.getId());
		} catch (Exception e) {
			System.out.println("\n❌ Erro ao criar postagem: " + e.getMessage());
		}
		aguardarEnter();
	}

	private void exibirDetalhesPostagem(Postagem postagem) {
		System.out.println("\n🆔 ID: " + postagem.getId());
		System.out.println("👤 Por: " + postagem.getNomeUsuario());
		System.out.println("📍 Local: " + postagem.getCidade() + "/" + postagem.getEstado());
		System.out.println("📅 Data: " + postagem.getDataHora().format(dateTimeFormatter));
		System.out.println("📝 Descrição: " + postagem.getDescricao());

		if (postagem.getRota() != null) {
			System.out.println("🛣️ Rota associada: " + postagem.getRota().getNome());
		}

		System.out.println("----------------------------------");
	}

	private void atualizarRota() {
		Long id = lerLong("\n🔍 Digite o ID da rota a atualizar (ou 0 para cancelar): ");
		if (id == 0) return;

		Optional<Rota> rotaExistente = rotaService.buscarPorId(id);
		if (rotaExistente.isEmpty()) {
			System.out.println("\n⚠️ Rota não encontrada!");
			aguardarEnter();
			return;
		}

		System.out.println("\n📝 Editando rota (deixe em branco para manter o valor atual)");
		Rota rotaAtualizada = new Rota();

		rotaAtualizada.setNome(lerString("Nome [" + rotaExistente.get().getNome() + "]: "));
		rotaAtualizada.setDistanciaKm(lerDoubleOuNulo("Distância em km [" + rotaExistente.get().getDistanciaKm() + "]: "));
		rotaAtualizada.setNivel(lerString("Nível de dificuldade [" + rotaExistente.get().getNivel() + "]: "));
		rotaAtualizada.setTipo(lerString("Tipo de trajeto [" + rotaExistente.get().getTipo() + "]: "));
		rotaAtualizada.setQualidadeAr(lerString("Qualidade do ar [" + rotaExistente.get().getQualidadeAr() + "]: "));
		rotaAtualizada.setPrevisaoTempo(lerString("Previsão do tempo [" + rotaExistente.get().getPrevisaoTempo() + "]: "));
		rotaAtualizada.setClima(lerString("Clima [" + rotaExistente.get().getClima() + "]: "));
		rotaAtualizada.setDescricao(lerString("Descrição [" + rotaExistente.get().getDescricao() + "]: "));

		try {
			Optional<Rota> rotaAtualizadaOpt = rotaService.atualizarRota(id, rotaAtualizada);
			if (rotaAtualizadaOpt.isPresent()) {
				System.out.println("\n✅ Rota atualizada com sucesso!");
			} else {
				System.out.println("\n⚠️ Não foi possível atualizar a rota!");
			}
		} catch (Exception e) {
			System.out.println("\n❌ Erro ao atualizar rota: " + e.getMessage());
		}
		aguardarEnter();
	}

	private void excluirRota() {
		Long id = lerLong("\n❌ Digite o ID da rota a excluir (ou 0 para cancelar): ");
		if (id == 0) return;

		if (lerSimNao("Tem certeza que deseja excluir esta rota? (S/N): ")) {
			boolean sucesso = rotaService.deletarRota(id);
			if (sucesso) {
				System.out.println("\n✅ Rota excluída com sucesso!");
			} else {
				System.out.println("\n⚠️ Rota não encontrada!");
			}
		} else {
			System.out.println("Operação cancelada.");
		}
		aguardarEnter();
	}

	private Double lerDoubleOuNulo(String mensagem) {
		while (true) {
			System.out.print(mensagem);
			String input = scanner.nextLine();
			if (input.isEmpty()) {
				return null;
			}
			try {
				return Double.parseDouble(input);
			} catch (NumberFormatException e) {
				System.out.println("❌ Valor inválido! Digite um número ou deixe em branco.");
			}
		}
	}

	private boolean lerSimNao(String mensagem) {
		while (true) {
			System.out.print(mensagem);
			String input = scanner.nextLine().trim().toUpperCase();
			if (input.equals("S")) return true;
			if (input.equals("N")) return false;
			System.out.println("❌ Digite S ou N!");
		}
	}

	private void registrarRotaRealizada() {
		System.out.println("\n📝 Registrando rota realizada...");

		RotaRealizada rotaRealizada = new RotaRealizada();

		String cpfUsuario = lerStringObrigatoria("CPF do usuário: ");
		Usuario usuario = new Usuario();
		usuario.setCpf(cpfUsuario);
		rotaRealizada.setUsuario(usuario);

		Long rotaId = lerLong("ID da rota realizada: ");
		Rota rota = new Rota();
		rota.setId(rotaId);
		rotaRealizada.setRota(rota);

		rotaRealizada.setData(lerData("Data da realização"));

		try {
			RotaRealizada registroSalvo = rotaRealizadaService.registrarRotaRealizada(rotaRealizada);
			System.out.println("\n✅ Rota registrada com sucesso! ID: " + registroSalvo.getId());

			Optional<Rota> rotaInfo = rotaService.buscarPorId(rotaId);
			rotaInfo.ifPresent(r -> {
				System.out.println("\nℹ️ Detalhes da Rota:");
				System.out.println("Nome: " + r.getNome());
				System.out.println("Distância: " + r.getDistanciaKm() + " km");
				System.out.println("Dificuldade: " + r.getNivel());
			});
		} catch (Exception e) {
			System.out.println("\n❌ Erro ao registrar rota: " + e.getMessage());
		}
		aguardarEnter();
	}

	private LocalTime lerHorario(String mensagem) {
		while (true) {
			try {
				System.out.print(mensagem + " (HH:MM): ");
				String input = scanner.nextLine();
				return LocalTime.parse(input, timeFormatter);
			} catch (Exception e) {
				System.out.println("❌ Horário inválido! Use o formato HH:MM.");
			}
		}
	}

	private void exibirDetalhesRota(Rota rota) {
		System.out.println("\n🆔 ID: " + rota.getId());
		System.out.println("🛣️ Nome: " + rota.getNome());
		System.out.println("📏 Distância: " + rota.getDistanciaKm() + " km");
		System.out.println("⚡ Nível: " + rota.getNivel());
		System.out.println("🏷️ Tipo: " + rota.getTipo());
		System.out.println("🌿 Qualidade do Ar: " + (rota.getQualidadeAr() != null ? rota.getQualidadeAr() : "Não informada"));
		System.out.println("🌤️ Previsão: " + (rota.getPrevisaoTempo() != null ? rota.getPrevisaoTempo() : "Não informada"));
		System.out.println("☀️ Clima: " + (rota.getClima() != null ? rota.getClima() : "Não informado"));
		System.out.println("📝 Descrição: " + (rota.getDescricao() != null ? rota.getDescricao() : "Nenhuma"));
		System.out.println("----------------------------------");
	}

	private void gerenciarParticipantes() {
		boolean voltar = false;

		while (!voltar) {
			System.out.println("\n=== GERENCIAMENTO DE PARTICIPANTES ===");
			System.out.println("1 - Listar todos os participantes");
			System.out.println("2 - Buscar participante por ID");
			System.out.println("3 - Cadastrar novo participante");
			System.out.println("4 - Atualizar participante");
			System.out.println("5 - Remover participante");
			System.out.println("6 - Transferir participante para outro grupo");
			System.out.println("0 - Voltar ao menu principal");
			System.out.print("Escolha uma opção: ");

			String opcao = scanner.nextLine();

			switch (opcao) {
				case "1":
					listarTodosParticipantes();
					break;
				case "2":
					buscarParticipantePorId();
					break;
				case "3":
					cadastrarNovoParticipante();
					break;
				case "4":
					atualizarParticipante();
					break;
				case "5":
					removerParticipante();
					break;
				case "6":
					transferirParticipante();
					break;
				case "0":
					voltar = true;
					break;
				default:
					System.out.println("❌ Opção inválida!");
			}
		}
	}

	private void listarTodosParticipantes() {
		System.out.println("\n🔍 Listando todos os participantes...");
		List<ParticipanteGrupo> participantes = participanteService.listarTodos();

		if (participantes.isEmpty()) {
			System.out.println("\n⚠️ Nenhum participante cadastrado!");
		} else {
			System.out.println("\n=== PARTICIPANTES CADASTRADOS ===");
			participantes.forEach(this::exibirDetalhesParticipante);
		}
		aguardarEnter();
	}

	private void buscarParticipantePorId() {
		Long id = lerLong("\n🔍 Digite o ID do participante (ou 0 para cancelar): ");
		if (id == 0) return;

		Optional<ParticipanteGrupo> participante = participanteService.buscarPorId(id);
		if (participante.isPresent()) {
			System.out.println("\n=== DETALHES DO PARTICIPANTE ===");
			exibirDetalhesParticipante(participante.get());
		} else {
			System.out.println("\n⚠️ Participante não encontrado!");
		}
		aguardarEnter();
	}

	private void cadastrarNovoParticipante() {
		System.out.println("\n👤 Cadastrando novo participante...");

		System.out.println("\n=== GRUPOS DISPONÍVEIS ===");
		grupoService.listarTodosGrupos().forEach(g ->
				System.out.println("ID: " + g.getId() + " | " + g.getNome()));

		ParticipanteGrupo participante = new ParticipanteGrupo();

		participante.setNome(lerStringObrigatoria("Nome do participante: "));
		participante.setTelefone(lerString("Telefone (opcional): "));

		Long grupoId = lerLong("ID do grupo: ");
		Grupo grupo = new Grupo();
		grupo.setId(grupoId);
		participante.setGrupo(grupo);

		try {
			ParticipanteGrupo participanteSalvo = participanteService.criarParticipante(participante);
			System.out.println("\n✅ Participante cadastrado com sucesso! ID: " + participanteSalvo.getId());
		} catch (Exception e) {
			System.out.println("\n❌ Erro ao cadastrar participante: " + e.getMessage());
		}
		aguardarEnter();
	}

	private void atualizarParticipante() {
		Long id = lerLong("\n🔍 Digite o ID do participante a atualizar (ou 0 para cancelar): ");
		if (id == 0) return;

		Optional<ParticipanteGrupo> participanteExistente = participanteService.buscarPorId(id);
		if (participanteExistente.isEmpty()) {
			System.out.println("\n⚠️ Participante não encontrado!");
			aguardarEnter();
			return;
		}

		System.out.println("\n📝 Editando participante (deixe em branco para manter o valor atual)");
		ParticipanteGrupo participanteAtualizado = new ParticipanteGrupo();

		participanteAtualizado.setNome(lerString("Nome [" + participanteExistente.get().getNome() + "]: "));
		participanteAtualizado.setTelefone(lerString("Telefone [" +
				(participanteExistente.get().getTelefone() != null ? participanteExistente.get().getTelefone() : "Não informado") + "]: "));

		try {
			Optional<ParticipanteGrupo> participanteOpt = participanteService.atualizarParticipante(id, participanteAtualizado);
			if (participanteOpt.isPresent()) {
				System.out.println("\n✅ Participante atualizado com sucesso!");
			} else {
				System.out.println("\n⚠️ Não foi possível atualizar o participante!");
			}
		} catch (Exception e) {
			System.out.println("\n❌ Erro ao atualizar participante: " + e.getMessage());
		}
		aguardarEnter();
	}

	private void removerParticipante() {
		Long id = lerLong("\n❌ Digite o ID do participante a remover (ou 0 para cancelar): ");
		if (id == 0) return;

		if (lerSimNao("Tem certeza que deseja remover este participante? (S/N): ")) {
			boolean sucesso = participanteService.deletarParticipante(id);
			if (sucesso) {
				System.out.println("\n✅ Participante removido com sucesso!");
			} else {
				System.out.println("\n⚠️ Participante não encontrado!");
			}
		} else {
			System.out.println("Operação cancelada.");
		}
		aguardarEnter();
	}

	private void transferirParticipante() {
		Long participanteId = lerLong("\n🔍 Digite o ID do participante a transferir (ou 0 para cancelar): ");
		if (participanteId == 0) return;

		System.out.println("\n=== GRUPOS DISPONÍVEIS ===");
		grupoService.listarTodosGrupos().forEach(g ->
				System.out.println("ID: " + g.getId() + " | " + g.getNome()));

		Long novoGrupoId = lerLong("ID do novo grupo: ");

		try {

			Optional<ParticipanteGrupo> participanteOpt = participanteService.buscarPorId(participanteId);
			if (participanteOpt.isEmpty()) {
				System.out.println("\n⚠️ Participante não encontrado!");
				aguardarEnter();
				return;
			}

			ParticipanteGrupo participanteAtualizado = new ParticipanteGrupo();
			participanteAtualizado.setNome(participanteOpt.get().getNome());
			participanteAtualizado.setTelefone(participanteOpt.get().getTelefone());

			Grupo novoGrupo = new Grupo();
			novoGrupo.setId(novoGrupoId);
			participanteAtualizado.setGrupo(novoGrupo);

			Optional<ParticipanteGrupo> resultado = participanteService.atualizarParticipante(participanteId, participanteAtualizado);

			if (resultado.isPresent()) {
				System.out.println("\n✅ Participante transferido com sucesso para o grupo ID: " + novoGrupoId);
			} else {
				System.out.println("\n⚠️ Não foi possível transferir o participante!");
			}
		} catch (Exception e) {
			System.out.println("\n❌ Erro ao transferir participante: " + e.getMessage());
		}
		aguardarEnter();
	}

	private void exibirDetalhesParticipante(ParticipanteGrupo participante) {
		System.out.println("\n🆔 ID: " + participante.getId());
		System.out.println("👤 Nome: " + participante.getNome());
		System.out.println("📞 Telefone: " + (participante.getTelefone() != null ? participante.getTelefone() : "Não informado"));
		System.out.println("👥 Grupo: " + (participante.getGrupo() != null ?
				"ID " + participante.getGrupo().getId() + " - " + participante.getGrupo().getNome() : "Não associado"));
		System.out.println("----------------------------------");
	}
}