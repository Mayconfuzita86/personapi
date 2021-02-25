package one.digitalinnovation.personapi.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundExeption;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE; //instance vem do mapper

    //Retirado após add do @AllArgsConstructor
    //@Autowired
    //public PersonService(PersonRepository personRepository){
    //    this.personRepository = personRepository;
    //    }

    public MessageResponseDTO createPerson(PersonDTO personDTO){
        Person personToSave = personMapper.toModel(personDTO); // conversão do DTO em uma única entidade

                //= Person.builder()
                //.firstName(personDTO.getFirstName())
                //.lastName(personDTO.getLastName())
                //.birthDate(personDTO.getBirthDate())
                //.phones(personDTO.getPhones())
                //.build();

        Person savedPerson = personRepository.save(personToSave); //criar uma nova pessoa
        return createMessageResponse(savedPerson.getId(), "Created person with ID ");
    }

    public List<PersonDTO> listAll(){
        List<Person> allPeople = personRepository.findAll();
        return allPeople.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundExeption {
        Person person = verifyIfExists(id);
        return personMapper.toDTO(person);
    }

    public void delete(Long id) throws PersonNotFoundExeption {
        verifyIfExists(id);

        personRepository.deleteById(id);
    }

   public MessageResponseDTO updateById(Long id, PersonDTO personDTO) throws PersonNotFoundExeption {

        verifyIfExists(id);

        Person personToUpdate = personMapper.toModel(personDTO);
        Person updatedPerson = personRepository.save(personToUpdate);
        return createMessageResponse(updatedPerson.getId(), "Updated person with ID ");
    }

    private Person verifyIfExists(Long id) throws PersonNotFoundExeption {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundExeption(id));
    }

    private MessageResponseDTO createMessageResponse(Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }
}
