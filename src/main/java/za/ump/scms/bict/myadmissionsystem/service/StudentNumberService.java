package za.ump.scms.bict.myadmissionsystem.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.Random;
import za.ump.scms.bict.myadmissionsystem.model.StudentNumberState;
import za.ump.scms.bict.myadmissionsystem.repository.StudentNumberStateRepository;

@Stateless
public class StudentNumberService {

    @Inject
    private StudentNumberStateRepository stateRepo;

    public String generateStudentNumber() {
        StudentNumberState state = stateRepo.getOrCreateState();

        int prefix = state.getCurrentPrefix();
        int suffixLength = 9 - String.valueOf(prefix).length();

        Random random = new Random();
        String suffix;


        do {
            int max = (int) Math.pow(10, suffixLength);
            suffix = String.format("%0" + suffixLength + "d", random.nextInt(max));
        } while (suffix.startsWith("0")); 

        String studentNumber = prefix + suffix;

        
        state.setCurrentPrefix(nextPrefix(prefix));
        stateRepo.update(state);

        return studentNumber;
    }

    private int nextPrefix(int current) {
       
        if (current % 10 == 9) {
            int base = current / 10;
            return base * 10 + 1; 
        } else {
            return current + 1;
        }
    }

    public String generateFiveDigitPin() {
        Random random = new Random();
        int firstDigit = random.nextInt(9) + 1;
        int middleDigits = random.nextInt(1000);
        int lastDigit;
        do {
            lastDigit = random.nextInt(10);
        } while (lastDigit == 0);

        return firstDigit + String.format("%03d", middleDigits) + lastDigit;
    }
}
