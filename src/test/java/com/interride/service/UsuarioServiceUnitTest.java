package com.interride.service;

import com.interride.dto.request.ActualizarUsuarioPerfilRequest;
import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.request.LoginRequest;
import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.dto.response.AuthResponse;
import com.interride.dto.response.UsuarioResponse;
import com.interride.exception.DuplicateResourceException;
import com.interride.mapper.UsuarioMapper;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Role;
import com.interride.model.entity.Usuario;
import com.interride.model.enums.ERole;
import com.interride.repository.ConductorRepository;
import com.interride.repository.PasajeroRepository;
import com.interride.repository.RoleRepository;
import com.interride.repository.UsuarioRepository;
import com.interride.security.TokenProvider;
import com.interride.service.Impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.interride.security.UserPrincipal;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsuarioServiceUnitTest {
    private PasajeroRegistrationRequest requestPasajero;
    private ConductorRegistroRequest requestConductor;
    private ActualizarUsuarioPerfilRequest requestActualizarUsuario;

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private ConductorRepository conductorRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private RoleRepository roleRepository;
    @Mock private UsuarioMapper usuarioMapper;

    @Mock private AuthenticationManager authenticationManager;
    @Mock private TokenProvider tokenProvider;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void  setUp(){
        MockitoAnnotations.openMocks(this);

        requestPasajero = new PasajeroRegistrationRequest(
                "Alan",
                "Doe",
                "alan@gmail.com",
                "password123",
                "1234567890",
                "alan_doe"
        );

        requestConductor = new ConductorRegistroRequest(
                "John",
                "Smith",
                "john@gmail.com",
                "987654321",
                "john_smith",
                "password123");

        requestActualizarUsuario = new ActualizarUsuarioPerfilRequest(
                "Jane",
                "Doe",
                "987654321123",
                "nuevo@gmail.com",
                "jane_doe");
    }

    //TESTS
    @Test
    @DisplayName("CP01 - Registrar pasajero exitosamente")
    void registrarPasajero_success() {
        Role role = new Role();
        role.setNombre(ERole.PASAJERO);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo("alan@gmail.com");
        usuario.setPassword("encodedPassword");
        usuario.setRole(role);

        Pasajero pasajero = new Pasajero();
        pasajero.setId(1);
        pasajero.setNombre("Alan");
        pasajero.setApellidos("Doe");
        pasajero.setTelefono("1234567890");
        pasajero.setUsername("alan_doe");
        pasajero.setFechaHoraRegistro(LocalDateTime.now());

        pasajero.setUsuario(usuario);
        usuario.setPasajero(pasajero);

        UsuarioResponse expectedResponse = new UsuarioResponse(
                1,
                "Alan",
                "Doe",
                "alan@gmail.com",
                "1234567890",
                "alan_doe",
                LocalDateTime.now().toString(),
                "Sin actualizar",
                ERole.PASAJERO.name()
        );

        when(usuarioRepository.existsByCorreo(requestPasajero.getCorreo())).thenReturn(false);
        when(pasajeroRepository.existsByUsername(requestPasajero.getUsername())).thenReturn(false);
        when(pasajeroRepository.existsByTelefono(requestPasajero.getTelefono())).thenReturn(false);
        when(roleRepository.findByNombre(ERole.PASAJERO)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioMapper.toEntity(requestPasajero)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toResponse(usuario, ERole.PASAJERO)).thenReturn(expectedResponse);


        UsuarioResponse resultado = usuarioService.registrarPasajero(requestPasajero);

        assertEquals(expectedResponse.nombre(), resultado.nombre());
        assertEquals(expectedResponse.apellidos(), resultado.apellidos());
        assertEquals(expectedResponse.correo(), resultado.correo());
        assertEquals(expectedResponse.telefono(), resultado.telefono());
        assertEquals(expectedResponse.username(), resultado.username());
        assertEquals(expectedResponse.role(), resultado.role());
    }

    @Test
    @DisplayName("CP02 - Registrar pasajero con correo existente")
    void registrarPasajero_duplicateEmail() {
        when(usuarioRepository.existsByCorreo(requestPasajero.getCorreo())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.registrarPasajero(requestPasajero)
        );

        System.out.println(exception.getMessage());
    }


    @Test
    @DisplayName("CP03 - Registrar pasajero con nombre de usuario existente")
    void registrarPasajero_duplicateUsername() {
        when(usuarioRepository.existsByCorreo(requestPasajero.getCorreo())).thenReturn(false);
        when(pasajeroRepository.existsByUsername(requestPasajero.getUsername())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.registrarPasajero(requestPasajero)
        );

        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP04 - Registrar pasajero con teléfono existente")
    void registrarPasajero_duplicatePhone() {
        when(usuarioRepository.existsByCorreo(requestPasajero.getCorreo())).thenReturn(false);
        when(pasajeroRepository.existsByUsername(requestPasajero.getUsername())).thenReturn(false);
        when(pasajeroRepository.existsByTelefono(requestPasajero.getTelefono())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.registrarPasajero(requestPasajero)
        );

        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP05 - Registrar conductor exitosamente")
    void registrarConductor_success() {
        Role role = new Role();
        role.setNombre(ERole.CONDUCTOR);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo("john@gmail.com");
        usuario.setPassword("encodedPassword");
        usuario.setRole(role);

        Conductor conductor = new Conductor();
        conductor.setId(1);
        conductor.setNombre("John");
        conductor.setApellidos("Smith");
        conductor.setTelefono("987654321");
        conductor.setUsername("john_smith");
        conductor.setFechaHoraRegistro(LocalDateTime.now());

        conductor.setUsuario(usuario);
        usuario.setConductor(conductor);

        UsuarioResponse expectedResponse = new UsuarioResponse(
                1,
                "John",
                "Smith",
                "john@gmail.com",
                "987654321",
                "john_smith",
                LocalDateTime.now().toString(),
                "Sin actualizar",
                ERole.CONDUCTOR.name()
        );

        when(usuarioRepository.existsByCorreo(requestConductor.correo())).thenReturn(false);
        when(conductorRepository.existsByUsername(requestConductor.username())).thenReturn(false);
        when(conductorRepository.existsByTelefono(requestConductor.telefono())).thenReturn(false);
        when(roleRepository.findByNombre(ERole.CONDUCTOR)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioMapper.toEntity(requestConductor)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toResponse(usuario, ERole.CONDUCTOR)).thenReturn(expectedResponse);

        UsuarioResponse resultado = usuarioService.registrarConductor(requestConductor);

        assertEquals(expectedResponse.nombre(), resultado.nombre());
        assertEquals(expectedResponse.apellidos(), resultado.apellidos());
        assertEquals(expectedResponse.correo(), resultado.correo());
        assertEquals(expectedResponse.telefono(), resultado.telefono());
        assertEquals(expectedResponse.username(), resultado.username());
        assertEquals(expectedResponse.role(), resultado.role());
    }

    @Test
    @DisplayName("CP06 - Registrar conductor con correo existente")
    void registrarConductor_duplicateEmail() {
        when(usuarioRepository.existsByCorreo(requestConductor.correo())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.registrarConductor(requestConductor)
        );

        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP07 - Registrar conductor con nombre de usuario existente")
    void registrarConductor_duplicateUsername() {
        when(usuarioRepository.existsByCorreo(requestConductor.correo())).thenReturn(false);
        when(conductorRepository.existsByUsername(requestConductor.username())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.registrarConductor(requestConductor)
        );

        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP08 - Registrar conductor con teléfono existente")
    void registrarConductor_duplicatePhone() {
        when(usuarioRepository.existsByCorreo(requestConductor.correo())).thenReturn(false);
        when(conductorRepository.existsByUsername(requestConductor.username())).thenReturn(false);
        when(conductorRepository.existsByTelefono(requestConductor.telefono())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.registrarConductor(requestConductor)
        );

        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP09 - Obtener usuario por ID exitosamente")
    void obtenerUsuarioPorId_success() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo("example@gmail.com");
        usuario.setRole(new Role());
        usuario.getRole().setNombre(ERole.PASAJERO);

        Pasajero pasajero = new Pasajero();
        pasajero.setId(1);
        pasajero.setNombre("John");
        pasajero.setApellidos("Doe");
        pasajero.setTelefono("1234567890");
        pasajero.setUsername("john_doe");
        pasajero.setFechaHoraRegistro(LocalDateTime.now());
        pasajero.setUsuario(usuario);
        usuario.setPasajero(pasajero);


        UsuarioResponse expectedResponse = new UsuarioResponse(
                1,
                "John",
                "Doe",
                "example@gmail.com",
                "1234567890",
                "john_doe",
                LocalDateTime.now().toString(),
                "sin actualizar",
                ERole.PASAJERO.name());


        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toResponse(usuario, ERole.PASAJERO)).thenReturn(expectedResponse);

        UsuarioResponse resultado = usuarioService.obtenerUsuarioPorId(1);

        assertEquals(expectedResponse.id(), resultado.id());
        assertEquals(expectedResponse.nombre(), resultado.nombre());
        assertEquals(expectedResponse.apellidos(), resultado.apellidos());
        assertEquals(expectedResponse.correo(), resultado.correo());
        assertEquals(expectedResponse.telefono(), resultado.telefono());
        assertEquals(expectedResponse.username(), resultado.username());
        assertEquals(expectedResponse.role(), resultado.role());
    }

    @Test
    @DisplayName("CP10 - Obtener usuario por ID no encontrado")
    void obtenerUsuarioPorId_notFound() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.obtenerUsuarioPorId(1));
    }

    @Test
    @DisplayName("CP11 - Actualizar usuario Pasajero exitosamente")
    void actualizarUsuarioPasajero_success() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo("antiguo@gmail.com");
        usuario.setRole(new Role());
        usuario.getRole().setNombre(ERole.PASAJERO);
        usuario.setPassword("encodedPassword");
        Pasajero pasajero = new Pasajero();
        pasajero.setId(1);
        pasajero.setNombre("John");
        pasajero.setApellidos("Doe");
        pasajero.setTelefono("1234567890");
        pasajero.setUsername("john_doe");
        pasajero.setFechaHoraRegistro(LocalDateTime.now());
        pasajero.setFechaHoraActualizacion(LocalDateTime.now());

        pasajero.setUsuario(usuario);
        usuario.setPasajero(pasajero);

        UsuarioResponse expectedResponse = new UsuarioResponse(
                1,
                "Jane",
                "Doe",
                "nuevo@gmail.com",
                "987654321",
                "jane_doe",
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString(),
                ERole.PASAJERO.name());

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByCorreo(requestActualizarUsuario.correo())).thenReturn(false);
        when(pasajeroRepository.existsByUsername(requestActualizarUsuario.username())).thenReturn(false);
        when(pasajeroRepository.existsByTelefono(requestActualizarUsuario.telefono())).thenReturn(false);
        when(conductorRepository.existsByUsername(requestActualizarUsuario.username())).thenReturn(false);
        when(conductorRepository.existsByTelefono(requestActualizarUsuario.telefono())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponse(any(Usuario.class), any(ERole.class))).thenReturn(expectedResponse);

        UsuarioResponse resultado = usuarioService.actualizarUsuario(1, requestActualizarUsuario);

        assertEquals(expectedResponse.id(), resultado.id());
        assertEquals(expectedResponse.nombre(), resultado.nombre());
        assertEquals(expectedResponse.apellidos(), resultado.apellidos());
        assertEquals(expectedResponse.correo(), resultado.correo());
        assertEquals(expectedResponse.telefono(), resultado.telefono());
        assertEquals(expectedResponse.username(), resultado.username());
        assertEquals(expectedResponse.role(), resultado.role());
    }

    @Test
    @DisplayName("CP12 - Actualizar usuario Conductor exitosamente")
    void actualizarUsuarioConductor_success() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo("antiguo@gmail.com");
        usuario.setRole(new Role());
        usuario.getRole().setNombre(ERole.PASAJERO);
        usuario.setPassword("encodedPassword");
        Conductor conductor = new Conductor();
        conductor.setId(1);
        conductor.setNombre("John");
        conductor.setApellidos("Doe");
        conductor.setTelefono("1234567890");
        conductor.setUsername("john_doe");
        conductor.setFechaHoraRegistro(LocalDateTime.now());
        conductor.setFechaHoraActualizacion(LocalDateTime.now());

        conductor.setUsuario(usuario);
        usuario.setConductor(conductor);

        UsuarioResponse expectedResponse = new UsuarioResponse(
                1,
                "Jane",
                "Doe",
                "nuevo@gmail.com",
                "987654321",
                "jane_doe",
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString(),
                ERole.PASAJERO.name());

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByCorreo(requestActualizarUsuario.correo())).thenReturn(false);
        when(pasajeroRepository.existsByUsername(requestActualizarUsuario.username())).thenReturn(false);
        when(pasajeroRepository.existsByTelefono(requestActualizarUsuario.telefono())).thenReturn(false);
        when(conductorRepository.existsByUsername(requestActualizarUsuario.username())).thenReturn(false);
        when(conductorRepository.existsByTelefono(requestActualizarUsuario.telefono())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponse(any(Usuario.class), any(ERole.class))).thenReturn(expectedResponse);

        UsuarioResponse resultado = usuarioService.actualizarUsuario(1, requestActualizarUsuario);

        assertEquals(expectedResponse.id(), resultado.id());
        assertEquals(expectedResponse.nombre(), resultado.nombre());
        assertEquals(expectedResponse.apellidos(), resultado.apellidos());
        assertEquals(expectedResponse.correo(), resultado.correo());
        assertEquals(expectedResponse.telefono(), resultado.telefono());
        assertEquals(expectedResponse.username(), resultado.username());
        assertEquals(expectedResponse.role(), resultado.role());
    }

    @Test
    @DisplayName("CP13 - Actualizar usuario con correo existente")
    void actualizarUsuario_duplicateEmail() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo("example@gmail.com");
        usuario.setPassword("encodedPassword");
        usuario.setRole(new Role());
        usuario.getRole().setNombre(ERole.PASAJERO);

        Pasajero pasajero = new Pasajero();
        pasajero.setId(1);
        pasajero.setNombre("John");
        pasajero.setApellidos("Doe");
        pasajero.setTelefono("1234567890");
        pasajero.setUsername("john_doe");
        pasajero.setFechaHoraRegistro(LocalDateTime.now());
        pasajero.setFechaHoraActualizacion(LocalDateTime.now());

        pasajero.setUsuario(usuario);
        usuario.setPasajero(pasajero);


        when(usuarioRepository.existsByCorreo(requestActualizarUsuario.correo())).thenReturn(true);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.actualizarUsuario(1, requestActualizarUsuario));

        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP14 - Actualizar usuario con nombre de usuario de pasajero existente")
    void actualizarUsuario_duplicateUsernamePasajero() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo("example@gmail.com");
        usuario.setPassword("encodedPassword");
        usuario.setRole(new Role());
        usuario.getRole().setNombre(ERole.PASAJERO);

        Pasajero pasajero = new Pasajero();
        pasajero.setId(1);
        pasajero.setNombre("John");
        pasajero.setApellidos("Doe");
        pasajero.setTelefono("1234567890");
        pasajero.setUsername("john_doe");
        pasajero.setFechaHoraRegistro(LocalDateTime.now());
        pasajero.setFechaHoraActualizacion(LocalDateTime.now());

        pasajero.setUsuario(usuario);
        usuario.setPasajero(pasajero);

        when(usuarioRepository.existsByCorreo(requestActualizarUsuario.correo())).thenReturn(false);
        when(pasajeroRepository.existsByUsername(requestActualizarUsuario.username())).thenReturn(true);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.actualizarUsuario(1, requestActualizarUsuario));

        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP15 - Actualizar usuario con teléfono de pasajero existente")
    void actualizarUsuario_duplicatePhonePasajero() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo("example@gmail.com");
        usuario.setPassword("encodedPassword");
        usuario.setRole(new Role());
        usuario.getRole().setNombre(ERole.PASAJERO);

        Pasajero pasajero = new Pasajero();
        pasajero.setId(1);
        pasajero.setNombre("John");
        pasajero.setApellidos("Doe");
        pasajero.setTelefono("1234567890");
        pasajero.setUsername("john_doe");
        pasajero.setFechaHoraRegistro(LocalDateTime.now());
        pasajero.setFechaHoraActualizacion(LocalDateTime.now());

        pasajero.setUsuario(usuario);
        usuario.setPasajero(pasajero);

        when(usuarioRepository.existsByCorreo(requestActualizarUsuario.correo())).thenReturn(false);
        when(pasajeroRepository.existsByUsername(requestActualizarUsuario.username())).thenReturn(false);
        when(pasajeroRepository.existsByTelefono(requestActualizarUsuario.telefono())).thenReturn(true);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.actualizarUsuario(1, requestActualizarUsuario));

        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP16 - Actualizar usuario con nombre de usuario de conductor existente")
    void actualizarUsuario_duplicateUsernameConductor() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo("example@gmail.com");
        usuario.setPassword("encodedPassword");
        usuario.setRole(new Role());
        usuario.getRole().setNombre(ERole.PASAJERO);

        Conductor conductor = new Conductor();
        conductor.setId(1);
        conductor.setNombre("John");
        conductor.setApellidos("Doe");
        conductor.setTelefono("1234567890");
        conductor.setUsername("john_doe");
        conductor.setFechaHoraRegistro(LocalDateTime.now());
        conductor.setFechaHoraActualizacion(LocalDateTime.now());

        conductor.setUsuario(usuario);
        usuario.setConductor(conductor);

        when(usuarioRepository.existsByCorreo(requestActualizarUsuario.correo())).thenReturn(false);
        when(conductorRepository.existsByUsername(requestActualizarUsuario.username())).thenReturn(true);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.actualizarUsuario(1, requestActualizarUsuario));

        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP17 - Actualizar usuario con teléfono de conductor existente")
    void actualizarUsuario_duplicatePhoneConductor() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo("example@gmail.com");
        usuario.setPassword("encodedPassword");
        usuario.setRole(new Role());
        usuario.getRole().setNombre(ERole.PASAJERO);

        Conductor conductor = new Conductor();
        conductor.setId(1);
        conductor.setNombre("John");
        conductor.setApellidos("Doe");
        conductor.setTelefono("1234567890");
        conductor.setUsername("john_doe");
        conductor.setFechaHoraRegistro(LocalDateTime.now());
        conductor.setFechaHoraActualizacion(LocalDateTime.now());

        conductor.setUsuario(usuario);
        usuario.setConductor(conductor);

        when(usuarioRepository.existsByCorreo(requestActualizarUsuario.correo())).thenReturn(false);
        when(conductorRepository.existsByUsername(requestActualizarUsuario.username())).thenReturn(false);
        when(conductorRepository.existsByTelefono(requestActualizarUsuario.telefono())).thenReturn(true);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.actualizarUsuario(1, requestActualizarUsuario));

        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP18 - Actualizar usuario con ID no encontrado")
    void actualizarUsuario_notFound() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.actualizarUsuario(1, requestActualizarUsuario));
    }


    @Test
    @DisplayName("CP19 - Login exitoso")
    void login_success() {
        AuthResponse expectedResponse = new AuthResponse();
        expectedResponse.setToken("token123");
        expectedResponse.setUsername("example@gmail.com");
        expectedResponse.setRole(ERole.PASAJERO.name());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCorreo("example@gmail.com");
        loginRequest.setPassword("<PASSWORD>");

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo("example@gmail.com");
        usuario.setPassword("encodedPassword");
        usuario.setRole(new Role());
        usuario.getRole().setNombre(ERole.PASAJERO);



        Authentication authentication = mock(Authentication.class);
        UserPrincipal userPrincipal = mock(UserPrincipal.class);


        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getUsuario()).thenReturn(usuario);

        when(tokenProvider.createAccessToken(authentication)).thenReturn("token123");
        when(usuarioMapper.toAuthResponse(usuario, "token123")).thenReturn(expectedResponse);

        AuthResponse resultado = usuarioService.login(loginRequest);

        assertEquals(expectedResponse.getToken(), resultado.getToken());
        assertEquals(expectedResponse.getUsername(), resultado.getUsername());
        assertEquals(expectedResponse.getRole(), resultado.getRole());
    }


}
