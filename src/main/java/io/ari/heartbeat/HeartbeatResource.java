package io.ari.heartbeat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/heartbeat")
public class HeartbeatResource {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity heartbeat() {
        return ResponseEntity.ok().build();
    }
}
