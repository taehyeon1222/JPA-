package nth.books;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationInfoService {

    private final CertificationInfoRepository certificationInfoRepository;
    public void create(BookDTO bookDTO,Book book) {
        CertificationInfo certificationInfo1 = new CertificationInfo();
        certificationInfo1.setCertificationName(bookDTO.getCertificationName());
        certificationInfo1.setBook(book);

        certificationInfoRepository.save(certificationInfo1);
    }
}
