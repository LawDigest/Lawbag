//package com.everyones.lawmaking.config;
//
//import com.everyones.lawmaking.domain.entity.Bill;
//import com.everyones.lawmaking.domain.entity.BillProposer;
//import com.everyones.lawmaking.domain.entity.Congressman;
//import com.everyones.lawmaking.domain.entity.Party;
//import com.everyones.lawmaking.repository.BillProposerRepository;
//import com.everyones.lawmaking.repository.BillRepository;
//import com.everyones.lawmaking.repository.CongressmanRepository;
//import com.everyones.lawmaking.repository.PartyRepository;
//import com.opencsv.CSVReader;
//import com.opencsv.CSVReaderBuilder;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import java.io.FileReader;
//import java.nio.charset.Charset;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Component
//@RequiredArgsConstructor
//public class dataMigration {
////    @Value("${csv.filepath.congressman}")
//    private static String CONGRESSMANCSVFILEPATH
//        = "C:/Users/ganjisriver/Desktop/csvfilepath/congressmandata.csv";
////    @Value("${csv.filepath.bill}")
//    private static String BILLCSVFILEPATH
//        = "C:/Users/ganjisriver/Desktop/csvfilepath/billdata.csv";
//    private final BillRepository billRepository;
//    private final CongressmanRepository congressmanRepository;
//    private final PartyRepository partyRepository;
//    private final BillProposerRepository billProposerRepository;
//
//    @PostConstruct
//    public void postConstruct() throws Exception {
//        // Party 데이터 넣기
//        List<Party> parties = new ArrayList<>();
//        parties.add(Party.builder().name("더불어민주당").build());
//        parties.add(Party.builder().name("국민의힘").build());
//        parties.add(Party.builder().name("정의당").build());
//        parties.add(Party.builder().name("진보당").build());
//        parties.add(Party.builder().name("기본소득당").build());
//        parties.add(Party.builder().name("한국의희망").build());
//        parties.add(Party.builder().name("무소속").build());
//        System.out.println(parties.get(0).toString());
//        partyRepository.saveAll(parties);
//        // 의원 데이터 넣기
//        System.out.println(CONGRESSMANCSVFILEPATH);
//        CSVReader congressManCsvReader = new CSVReaderBuilder(new FileReader(CONGRESSMANCSVFILEPATH, Charset.forName("EUC-KR")))
//                .withSkipLines(1)
//                .build();
//
//        List<Congressman> congressmanList = new ArrayList<>();
//        congressManCsvReader.forEach(line -> congressmanList.add(Congressman.builder()
//                .id(line[17])
//                .name(line[0])
//                .build()));
//        congressmanRepository.saveAll(congressmanList);
//        System.out.println("CongressManCSV데이터: "+ congressmanList.get(0).toString());
////         의안 데이터 넣기
//        CSVReader billCsvReader = new CSVReaderBuilder(new FileReader(BILLCSVFILEPATH, Charset.forName("EUC-KR")))
//                .withSkipLines(1)
//                .build();
//
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        for (String[] line: billCsvReader) {
//
//            List<Congressman> congressmen =  congressmanRepository.findByNameIn(line[9].split(","));
//            Congressman representProposer = congressmanRepository.findByName(line[8]);
//            Bill bill =Bill.builder()
//                    .id(line[0])
//                    .billName(line[2])
//                    .age(Integer.parseInt(line[6]))
//                    .representProposer(representProposer)
//                    .summary(line[13])
//                    .proposeDate(LocalDate.parse(line[4], formatter))
//                    .build();
//            Bill savedBill = billRepository.save(bill);
//
//            // 대표 발의자 넣기
//            billProposerRepository.save(BillProposer.builder()
//                            .bill(savedBill)
//                    .congressman(representProposer)
//                    .isRepresent(true)
//                    .build());
//
//            // 공동 발의자 넣기
//            for (Congressman congressman: congressmen) {
//                billProposerRepository.save(BillProposer.builder()
//                        .bill(savedBill)
//                        .congressman(congressman)
//                        .isRepresent(false)
//                        .build());
//            }
//        }
//
////        billRepository.saveAll(bills);
//    }
//
//}
