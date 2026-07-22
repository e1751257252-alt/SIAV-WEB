package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.request.UsuarioRequestDto;
import com.siav_pisip.siav_web.model.dto.response.UsuarioResponseDto;
import com.siav_pisip.siav_web.service.IRolService;
import com.siav_pisip.siav_web.service.IUsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private IUsuarioService servicioUsuario;

	@Autowired
	private IRolService servicioRol;

	@GetMapping
	public String leerpagina(Model model) {
		List<UsuarioResponseDto> usuariosBD = servicioUsuario.listarUsuarios();
		model.addAttribute("listausuarios", usuariosBD);
		return "usuario/listarUsuario";
	}

	@GetMapping("/crearUsuario")
	public String leerpaginacrear(Model model) {
		model.addAttribute("usuario", new UsuarioRequestDto());
		model.addAttribute("listaroles", servicioRol.listarRoles());
		model.addAttribute("listajefes", servicioUsuario.listarUsuarios());
		return "usuario/crearUsuario";
	}

	@PostMapping("/guardar")
	public String guardarUsuario(@ModelAttribute UsuarioRequestDto usuario) {
		servicioUsuario.guardarUsuario(usuario);
		return "redirect:/usuario";
	}

	@GetMapping("/editarUsuario/{idUsuario}")
	public String leerpaginaeditar(@PathVariable Long idUsuario, Model model) {
		UsuarioResponseDto existente = servicioUsuario.buscarPorId(idUsuario);
		UsuarioRequestDto usuario = new UsuarioRequestDto();
		usuario.setIdUsuario(existente.getIdUsuario());
		usuario.setCedula(existente.getCedula());
		usuario.setNombres(existente.getNombres());
		usuario.setApellidos(existente.getApellidos());
		usuario.setEmail(existente.getEmail());
		usuario.setIdRol(existente.getIdRol());
		usuario.setIdJefe(existente.getIdJefe());
		usuario.setCargo(existente.getCargo());
		usuario.setFechaIngreso(existente.getFechaIngreso());
		usuario.setActivo(existente.getActivo());
		model.addAttribute("usuario", usuario);
		model.addAttribute("listaroles", servicioRol.listarRoles());
		model.addAttribute("listajefes", servicioUsuario.listarUsuarios());
		return "usuario/editarUsuario";
	}

	@PostMapping("/desactivar/{idUsuario}")
	public String desactivarUsuario(@PathVariable Long idUsuario) {
		servicioUsuario.desactivarUsuario(idUsuario);
		return "redirect:/usuario";
	}

}