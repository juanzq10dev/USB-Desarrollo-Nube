import { useEffect, useState } from "react";
import { firebaseAuth } from "../firebase";
import { getProfile } from "../repositories/UserRepository";
import { useNavigate } from "react-router-dom";

export const useFirebaseAuth = () => {
    const [user, setUser] = useState(null);
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [profile, setProfile] = useState(null);


    useEffect(() => {
        const unsubscribe = firebaseAuth.onAuthStateChanged(user => {
            setUser(user);
            setLoading(false);
            if (!user) {
                return;
            }
            getProfile(user.email).then((theProfile) => {
                setProfile(theProfile);
            });
        });

        return unsubscribe;
    }, []);

    const doLogout = () => {
        firebaseAuth.signOut().then(() => {
            navigate('/');
        }).catch((error) => {
            console.log(error);
        });
    }
    return { user, loading, profile, doLogout };
}