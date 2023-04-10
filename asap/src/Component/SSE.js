import { useEffect, useState } from "react"

import "../style/SSE.css"

export default function SSE () {
    const [message, setMessage] = useState([])
    
    useEffect(() => {
        const eventSourse = new EventSource(`${process.env.REACT_APP_SERVER_URL}/connect`, {
            "withCredentials": true,
            'Content-Type': 'text/event-stream',
            'Cache-Control': 'no-cache',
            'Connection': 'keep-alive',
            "Accept-Encoding": "gzip,deflate, br",
            "Accept" : "text/event-stream"
        })

        eventSourse.addEventListener('sse', async (e) => {
            const newdata = e.data
            setMessage([...message, newdata])
        })
    }, [])

    return (
        <div className="sse">
            {message.length > 0 ? message.map((el,index) => {
                return (
                    <label key={index}>
                        <input type="checkbox" className="alertCheckbox" autoComplete="off" />
                        <div className="alert error">
                            <span className="alertClose">X</span>
                            <span className="alertText">{el}
                                <br className="clear"/>
                            </span>
                        </div>
                    </label>
                )
            }) : null}
        </div>
    )
}

//{"userId":"user","title":"입고 알림!","content":"맛잠 +160 입고","notificationType":"RECEIVE"}
