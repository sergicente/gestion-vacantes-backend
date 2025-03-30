package reto.configuration;


import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reto.model.dto.VacanteDto;
import reto.model.dto.VacanteInputDto;
import reto.model.dto.SolicitudDto;
import reto.model.dto.EmpresaDto;
import reto.model.entity.Vacante;
import reto.model.entity.Solicitud;
import reto.model.entity.Empresa;

@Configuration
public class RetoConfiguration {

    @Bean
    ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        modelMapper.createTypeMap(Vacante.class, VacanteDto.class);  
        modelMapper.createTypeMap(Solicitud.class, SolicitudDto.class);
        modelMapper.createTypeMap(Empresa.class, EmpresaDto.class);

        return modelMapper;
    }
}


