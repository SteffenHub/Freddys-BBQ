package LinkedInLearning.SpringBootApp;

import org.springframework.stereotype.Service;

@Service
public class HelloFrankService implements GrettingService{

    public String hello() {
        return "America, I'm only getting started.";
    }
    
}
