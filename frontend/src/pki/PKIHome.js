import React, {useEffect} from 'react';
import axios from 'axios';

function PKIHome (){

    useEffect(() => {
        let header ={
            
        }
        axios.get(`${process.env.REACT_APP_API_URL}/v1/certificates`)
    }, [])
    
    return (
        <div>
            PKIHome
        </div>
    )
}

export default PKIHome;