import { RecaptchaVerifier } from "firebase/auth";
import React, { useEffect, useState } from "react"
import { firebaseAuth } from "./firebase";
import { phoneLogin } from "./repositories/AuthRepository";
import { useNavigate } from "react-router-dom";
import { Button, Col, Container, Form, Row } from "react-bootstrap";

export const PhoneLogin = () => {
    const [number, setNumber] = useState('')
    const [verificationCode, setVerificationCode] = useState('')
    const navigate = useNavigate();
    const auth = firebaseAuth

    useEffect(() => {
        const verifier = new RecaptchaVerifier(auth, "sign-in-button", {
            size: "invisible",
        });
        window.recaptchaVerifier = verifier;

        return () => verifier.clear(); // Cleanup on unmount
    }, [auth]);


    const handleRegisterPhoneNumber = async (e) => {
        e.preventDefault();
        const appVerifier = await window.recaptchaVerifier
        phoneLogin(number, appVerifier, verificationCode).then(() => {
            navigate('/dashboard');
        }).catch((error) => {
            console.log(error);
        });
    }


    return (
        <Container>
            <Row>
                <Col>
                    <h1>Login with phone Number</h1>
                    <Form onSubmit={handleRegisterPhoneNumber}>
                        <Form.Group>
                            <Form.Label>Phone Number</Form.Label>
                            <Form.Control required value={number} onChange={(e) => setNumber(e.target.value)} type="text" placeholder="Enter phone number to Log In" />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Phone Number</Form.Label>
                            <Form.Control required value={verificationCode} onChange={(e) => setVerificationCode(e.target.value)} type="text" placeholder="Enter verification code" />
                        </Form.Group>
                        <Button id="sign-in-button" type="submit">Sign in</Button>
                    </Form>
                </Col>
            </Row>
        </Container>
    )
}