package co.edu.unal.clinica.mb;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Session;

import co.edu.unal.clinica.daos.UsuarioDAO;
import co.edu.unal.clinica.hibernate.data.Usuario;
import co.edu.unal.clinica.utils.HibernateUtil;

@ManagedBean(name="userMB")
@SessionScoped
public class UserMB {

	private String nickName;
	private String rol;
	private String password;
	private String nombre;
	private String apellidos;
	private List<Usuario> listaUsuarios;
	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	private Usuario usuario = new Usuario();

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public void guardarUsuario() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Usuario cliente = new Usuario(nickName, password, rol, nombre, apellidos);
		session.save(cliente);
		session.getTransaction().commit();
		session.close();
	}

	public void eliminar(Usuario emp) throws Exception {
		usuarioDAO.Eliminar(emp);
		this.listar();
	}

	public String leer(Usuario emp) {
		this.usuario = emp;
		return "editar";
	}
	
	public String modificar() throws Exception {
		usuarioDAO.Modificar(this.usuario);
		return "adminUsuarios";
	}

	public void listar() throws Exception {
		this.listaUsuarios = usuarioDAO.Listar();
	}

	public String verificarDatos() throws Exception {
		UsuarioDAO usuDAO = new UsuarioDAO();
		Usuario us;
		String resultado;

		try {
			this.usuario.setNickName(nickName);
			this.usuario.setPassword(password);

			us = usuDAO.verificarDatos(this.usuario);
			if (us != null) {

				FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().put("usuario", us);

				resultado = "routerAdmin"; // recalcar que el
											// faces-redirect=true,
											// olvida la peticion anterior y se
											// dirige a la vista
				// this.usuario = us;
			} else {
				resultado = "error";
			}
		} catch (Exception e) {
			throw e;
		}

		return resultado;
	}

	public boolean verificarSesion() {
		boolean estado;

		if (FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("usuario") == null) {
			estado = false;
		} else {
			estado = true;
		}

		return estado;
	}
	
	public String cerrarSesion() {
		FacesContext.getCurrentInstance().getExternalContext()
				.invalidateSession();
		return "index?faces-redirect=true";
	}
}
